import React, { useEffect, useState } from 'react';
import axios from 'axios';
import 'flowbite';

const OvertimeRequests = () => {
    const [requests, setRequests] = useState([]); // Ensure initial state is an array
    const [error, setError] = useState(null);

    useEffect(() => {
        // Replace with your actual API endpoint
        axios.get('http://localhost:8005/api/overtime')
            .then((response) => {
                // Ensure the response data is an array
                if (Array.isArray(response.data)) {
                    setRequests(response.data);
                } else {
                    throw new Error('Data is not an array');
                }
            })
            .catch((error) => {
                // Handle the error state
                console.error('Error fetching data: ', error);
                setError(error);
            });
    }, []);

    // Early return in case of error
    if (error) {
        return <div>Error fetching data: {error.message}</div>;
    }

    return (
        <div className="overflow-x-auto">
            <table className="min-w-full text-sm text-left text-gray-500 dark:text-gray-400">
                <thead className="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
                <tr>
                    <th scope="col" className="px-6 py-3">
                        Request ID
                    </th>
                    <th scope="col" className="px-6 py-3">
                        End Time
                    </th>
                    <th scope="col" className="px-6 py-3">
                        Is Approved
                    </th>
                    <th scope="col" className="px-6 py-3">
                        Reason
                    </th>
                    <th scope="col" className="px-6 py-3">
                        Start Time
                    </th>
                    <th scope="col" className="px-6 py-3">
                        User ID
                    </th>
                    <th scope="col" className="px-6 py-3">
                        Approval Status
                    </th>
                </tr>
                </thead>
                <tbody>
                {requests.map((request) => (
                    <tr key={request.id} className="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
                        <td className="px-6 py-4">{request.id}</td>
                        <td className="px-6 py-4">{request.endTime}</td>
                        <td className="px-6 py-4">{String(request.isApproved)}</td>
                        <td className="px-6 py-4">{request.reason}</td>
                        <td className="px-6 py-4">{request.startTime}</td>
                        <td className="px-6 py-4">{request.user_id}</td>
                        <td className="px-6 py-4">{request.approvalStatus}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default OvertimeRequests;
