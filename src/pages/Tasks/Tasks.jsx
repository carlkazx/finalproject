
import { Box, Typography } from '@mui/material';
import { DataGrid } from '@mui/x-data-grid';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import {useEffect, useState} from "react";


const Tickets = () => {
    const [rows, setRows] = useState([]);
    const navigate = useNavigate(); // useNavigate hook from React Router v6

    // Function to fetch data from the API using Axios
    const fetchData = async () => {
        console.log('Fetching tasks...');
        try {
            const response = await axios.get('http://localhost:8005/api/tasks');
            setRows(response.data.map(task => ({
                ...task,
                created: new Date(task.created).toLocaleString(),
                // Other transformations can be applied here as well
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

        // Cleanup interval on unmount
        return () => {
            console.log('Clearing tasks fetch interval');
            clearInterval(intervalId);
        };
    }, []);

    const columns = [
        { field: "ticketId",
            headerName: "Ticket ID",
            flex: 1,
            headerAlign:"center",
            align:"center",
        },


        { field: "description",
            headerName: "Summary",
            flex: 1,
            headerAlign:"center",
            align:"center", },

        {
            field: "assignedTo",
            headerName: "Assigned To",
            flex: 1,
            renderCell: (params) => {
                // Check if assignedTo is not null and has a property 'fullName'
                if (params.value && params.value.fullName) {
                    return <span>{params.value.fullName}</span>;
                } else {
                    // Return a default value or placeholder
                    return <span>Unassigned</span>;
                }
            },
            headerAlign:"center",
            align:"center",
        },

        {
            field: "status",
            headerName: "Status",
            flex: 1,
            renderCell: (params) => {
                // Check if the status is a string that needs to be parsed as JSON
                let statusValue;
                try {
                    const parsed = JSON.parse(params.value);
                    statusValue = parsed.status;
                } catch (e) {
                    // If parsing fails, it means it's not a JSON string, so just use the value directly
                    statusValue = params.value;
                }

                return <span>{statusValue}</span>;
            },
            headerAlign:"center",
            align:"center",
        },

        { field: "timestamp",
            headerName: "Created On",
            flex: 1,
            headerAlign:"center",
            align:"center",
        },

        {
            field: "dueDate",
            headerName: "Deadline",
            flex: 1,
// Optional: Format the date if required
            headerAlign:"center",
            align:"center",
        },
        {
            field: "priority",
            headerName: "Priority",
            flex: 1,
            renderCell: ({ value }) => {
                let color;
                switch (value) {
                    case 'HIGH':
                        color = '#FF0000'; // Red for high priority
                        break;
                    case 'MEDIUM':
                        color = '#FFA500'; // Orange for medium priority
                        break;
                    default:
                        color = '#FFFFFF'; // Default color
                }

                return (
                    <Box
                        width="100%"
                        display="flex"
                        alignItems="center"
                        justifyContent="center"
                        backgroundColor={color}
                        borderRadius="4px"
                    >
                        <Typography>{value}</Typography>

                    </Box>
                );
            },
            headerAlign:"center",
            align:"center",
        },
    ];

    return (
        <Box m="10px"
        >
            <Box
                m="-30px 0 0 0"
                height="77vh"
                sx={{
                    "& .MuiDataGrid-root": {
                        borderRadius:"35px"
                    },
                    "& .MuiDataGrid-cell": {
                        borderBottom: "1px solid #e0e0e0",
                        color: "rgba(0, 0, 0, 0.87)",
                    },
                    "& .name-column--cell": {
                        fontWeight: 'bold',
                    },
                    "& .MuiDataGrid-columnHeaders": {
                        color: "#ffffff",
                    },
                    "& .MuiDataGrid-virtualScroller": {
                        backgroundColor: "#ffffff",
                    },
                    "& .css-pdct74-MuiTablePagination-selectLabel": {
                        color:"#ffffff"
                    },
                    "& .css-16c50h-MuiInputBase-root-MuiTablePagination-select .MuiTablePagination-select": {
                        color:"#ffffff"
                    },
                    "& .css-levciy-MuiTablePagination-displayedRows": {
                        color:"#ffffff"
                    },
                    "& .MuiCheckbox-root": {
                        color: "inherit",
                    },
                }}
            >
                <DataGrid

                    rows={rows}
                    columns={columns}
                    onRowClick={(params) => {
                        // Redirect to TaskDetails page on row click using useNavigate
                        navigate(`/task/${params.id}`);
                    }}
                />
            </Box>
        </Box>
    );
}

export default Tickets;