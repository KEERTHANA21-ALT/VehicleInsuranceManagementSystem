import { useEffect, useState } from "react";
import axios from "axios";
import EmployeeNavbar from "./EmployeeNavbar";

function EmployeePolicy() {
    const [policies, setPolicies] = useState([]);
    const token = localStorage.getItem("token");

    const config = {
        headers: { Authorization: "Bearer " + token }
    };

    const fetchPolicies = async () => {
        try {
            const response = await axios.get("http://localhost:8080/api/policy/employee/get-all", config);
            console.log(response.data);
            setPolicies(response.data);
        } catch (error) {
            console.log(error);
        }
    };

    useEffect(() => {
        fetchPolicies();
    }, []);

    return (
        <>
            <EmployeeNavbar />
            <div className="container py-4">
                <h3>Created Policies</h3>
                <table className="table table-bordered shadow mt-3">
                    <thead className="table-primary">
                        <tr>
                            <th>S.No</th>
                            <th>Policy Number</th>
                            <th>Customer</th>
                            <th>Vehicle</th>
                            <th>Plan</th>
                            <th>Premium</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        {policies.map((policy, index) => (
                            <tr key={policy.policyNumber}>
                                <td>{index + 1}</td>
                                <td>{policy.policyNumber}</td>
                                <td>{policy.policyHolderName}</td>
                                <td>{policy.vehicleNumber}</td>
                                <td>{policy.planType}</td>
                                <td>₹ {policy.premiumAmount}</td>
                                <td>{policy.policyStatus}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </>
    );
}

export default EmployeePolicy;