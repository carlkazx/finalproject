import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { Dropdown } from 'flowbite-react';

const TaskDetails = () => {
    const [task, setTask] = useState({
        title: '',
        description: '',
        assignedTo: '',
        status: '',
        dueDate: '',
        dueTime: "00:00",
        priority: '',
    });
    const [users, setUsers] = useState([]);
    const { taskId } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        // This will run when the component mounts
        const savedTask = localStorage.getItem('taskDetails');
        if (savedTask) {
            setTask(JSON.parse(savedTask));
        }
        return () => {
            localStorage.setItem('taskDetails', JSON.stringify(task));
        }
    }, [taskId]); // You might want to save on taskId change or some other condition depending on your use case


    useEffect(() => {
        const fetchTask = async () => {
            try {
                const response = await axios.get(`http://localhost:8005/api/tasks/${taskId}`);
                setTask(response.data);
            } catch (error) {
                console.error('Error fetching task details:', error);
            }
        };

        const fetchUsers = async () => {
            try {
                const userResponse = await axios.get('http://localhost:8005/api/users');
                setUsers(userResponse.data);
            } catch (error) {
                console.error('Error fetching users:', error);
            }
        };

        fetchTask();
        fetchUsers();
    }, [taskId]);

    useEffect(() => {
        // Initialize selectedUserId when component mounts or taskId changes
        setSelectedUserId(task.assignedTo);
    }, [task.assignedTo, taskId]);

    const assignUser = async (userId) => {
        try {
            const response = await axios.post(`http://localhost:8005/api/tasks/${taskId}/assign`, {
                userId: userId
            });
            if (response.status === 200) {
                console.log('User assigned successfully');
            }
        } catch (error) {
            console.error('Error assigning user:', error);
        }
    };

    const [selectedUserId, setSelectedUserId] = useState(task.assignedTo);


    const handleInputChange = (e) => {
        const { name, value } = e.target;

        if (name === 'assignedTo') {
            setSelectedUserId(value); // Store the selected user ID
        } else if (name === 'dueDate' || name === 'dueTime') {
            setTask({ ...task, [name]: value });
        } else {
            setTask({ ...task, [name]: value });
        }
    };

    const handleStatusChange = async (newStatus) => {
        try {
            // Call the API to update the status
            const response = await axios.post(`http://localhost:8005/api/tasks/${taskId}/status`, { status: newStatus });

            // If the response is successful, update the task state
            if (response.status === 200) {
                setTask(prevTask => ({
                    ...prevTask,
                    status: newStatus
                }));
            }
        } catch (error) {
            console.error('Error updating task status:', error);
            // Handle the error (e.g., show an error message to the user)
        }
    };

    const handleUpdate = async () => {
        const date = new Date(task.dueDate);
        const time = task.dueTime || "00:00"; // Fallback to "00:00" if dueTime is undefined

        if (!isNaN(date)) {
            const [hours, minutes] = time.split(':').map(str => parseInt(str, 10));
            date.setHours(hours, minutes, 0, 0);

            const localTimeOffset = date.getTimezoneOffset() * 60000;
            const localDateTime = new Date(date.getTime() - localTimeOffset);
            const formattedDateTime = localDateTime.toISOString();

            const updatedTask = {
                ...task,
                dueDate: formattedDateTime,
                assignedTo: selectedUserId,
            };

            try {
                const response = await axios.put(`http://localhost:8005/api/tasks/${taskId}`, updatedTask);
                console.log('Task updated successfully', response.data);

                if (selectedUserId) {
                    await assignUser(selectedUserId);
                }

                navigate('/tasks');
            } catch (error) {
                console.error('Error updating task:', error);
            }
        } else {
            console.error('Invalid date format');
        }
    };



    if (!task) return <p>Loading...</p>;

    return (
        <div className="p-4 bg-white">
            <h4 className="text-2xl font-bold mb-4">Task Details</h4>

            <input
                type="text"
                className="block w-full px-4 py-2 mb-4 text-sm text-gray-900 bg-white border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500"
                name="title"
                placeholder="Title"
                value={task.title}
                onChange={handleInputChange}
            />

            <textarea
                className="block w-full px-4 py-2 mb-4 text-sm text-gray-900 bg-white border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500"
                name="description"
                placeholder="Description"
                rows={4}
                value={task.description}
                onChange={handleInputChange}
            />

            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                {/* Assigned To Dropdown */}
                <Dropdown label={users.find(user => user.id === selectedUserId)?.fullName || "Assigned To"}>
                    {users.map(user => (
                        <Dropdown.Item key={user.id} onClick={() => setSelectedUserId(user.id)}>
                            {user.fullName}
                        </Dropdown.Item>
                    ))}
                </Dropdown>


                {/* Status Dropdown */}
                <Dropdown label={task.status || "Status"}>
                    {["Open", "In Progress", "Closed"].map(status => (
                        <Dropdown.Item key={status} onClick={() => setTask({ ...task, status })}>
                            {status}
                        </Dropdown.Item>
                    ))}
                </Dropdown>

                {/* Priority Dropdown */}
                <Dropdown label={task.priority || "Priority"}>
                    {["High", "Medium", "Low"].map(priority => (
                        <Dropdown.Item key={priority} onClick={() => setTask({ ...task, priority })}>
                            {priority}
                        </Dropdown.Item>
                    ))}
                </Dropdown>

                {/* Due Date Input */}
                <input
                    type="date"
                    className="block w-full px-4 py-2 mb-4 text-sm text-gray-900 bg-white border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500"
                    name="dueDate"
                    value={task.dueDate ? task.dueDate.split('T')[0] : ""}
                    onChange={handleInputChange}
                />

                {/* Due Time Input */}
                <input
                    type="time"
                    className="block w-full px-4 py-2 mb-4 text-sm text-gray-900 bg-white border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500"
                    name="dueTime"
                    value={task.dueTime}
                    onChange={handleInputChange}
                />


            </div>

            <button
                className="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
                onClick={handleUpdate}
            >
                Update Task
            </button>
        </div>
    );
};

export default TaskDetails;
