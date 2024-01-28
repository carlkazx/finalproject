import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const Tickets = () => {
    const [rows, setRows] = useState([]);
    const navigate = useNavigate();

    const formatDate = (dateString) => {
        if (!dateString) {
            return "Date Not Available";
        }

        // Expected format: "M/D/YYYY H:M:S"
        const parts = dateString.split(/[\s/:]/);
        if (parts.length === 6) {
            const [month, day, year, hours, minutes, seconds] = parts.map(part => parseInt(part, 10));
            const date = new Date(year, month - 1, day, hours, minutes, seconds);
            return isNaN(date.getTime()) ? "Invalid Date" : date.toLocaleString();
        } else {
            return "Invalid Format";
        }
    };

    const formatISODate = (isoDateString) => {
        if (!isoDateString) {
            return "Date Not Available";
        }

        const date = new Date(isoDateString);
        return isNaN(date.getTime()) ? "Invalid Date" : date.toLocaleString();
    };


    const fetchData = async () => {
        console.log('Fetching tasks...');
        try {
            const response = await axios.get('http://localhost:8005/api/tasks');
            console.log('Tasks data:', response.data);
            setRows(response.data.map(task => ({
                ...task,
                created: formatDate(task.timestamp),
                dueDate: formatISODate(task.dueDate) // Use the separate function for dueDate
            })));
            console.log('Tasks fetched successfully:', response.data);
        } catch (error) {
            console.error('There was an error fetching the tasks:', error);
        }
    };


    useEffect(() => {
        fetchData();
        const intervalId = setInterval(() => {
            fetchData();
        }, 30000); // Refresh every 30 seconds

        return () => {
            console.log('Clearing tasks fetch interval');
            clearInterval(intervalId);
        };
    }, []);

    const tableCellStyle = "p-4 border-b border-gray-200 text-gray-600";
    const tableHeaderStyle = "p-4 text-gray-600 font-bold";

    return (
        <div className="m-4">
            <div className="overflow-x-auto relative shadow-md sm:rounded-lg">
                <table className="w-full text-sm text-left text-gray-500">
                    <thead className="text-xs text-gray-700 uppercase bg-gray-50">
                    <tr>
                        <th scope="col" className={tableHeaderStyle}>Ticket ID</th>
                        <th scope="col" className={tableHeaderStyle}>Summary</th>
                        <th scope="col" className={tableHeaderStyle}>Assigned To</th>
                        <th scope="col" className={tableHeaderStyle}>Status</th>
                        <th scope="col" className={tableHeaderStyle}>Created On</th>
                        <th scope="col" className={tableHeaderStyle}>Deadline</th>
                        <th scope="col" className={tableHeaderStyle}>Priority</th>
                    </tr>
                    </thead>
                    <tbody>
                    {rows.map((row, index) => (
                        <tr key={index} className="bg-white border-b hover:bg-gray-50 cursor-pointer" onClick={() => navigate(`/task/${row.id}`)}>
                            <td className={tableCellStyle}>{row.ticketId}</td>
                            <td className={tableCellStyle}>{row.description}</td>
                            <td className={tableCellStyle}>
                                {row.assignedTo && row.assignedTo.fullName ? row.assignedTo.fullName : "Unassigned"}
                            </td>
                            <td className={tableCellStyle}>
                                {(() => {
                                    try {
                                        const parsed = JSON.parse(row.status);
                                        return parsed.status;
                                    } catch {
                                        return row.status;
                                    }
                                })()}
                            </td>
                            <td className={tableCellStyle}>{row.created}</td>
                            <td className={tableCellStyle}>{row.dueDate}</td>
                            <td className={tableCellStyle}>
                                <div className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${row.priority === 'HIGH' ? 'bg-red-100 text-red-800' : row.priority === 'MEDIUM' ? 'bg-yellow-100 text-yellow-800' : 'bg-green-100 text-green-800'}`}>
                                    {row.priority}
                                </div>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}

export default Tickets;
