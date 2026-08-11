import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate, useParams } from "react-router";
import EmployeeNavbar from "./EmployeeNavbar";

function EmployeePolicyManagement() {
    const { proposalId } = useParams();
    const navigate = useNavigate();
    const [proposal, setProposal] = useState(null);

    const token = localStorage.getItem("token");
    const config = {
        headers: { Authorization: "Bearer " + token }
    };

    const fetchProposal = async () => {
        try {
            const response = await axios.get(
                `http://localhost:8080/api/proposal/get-one/employee/${proposalId}`,
                config
            );
            setProposal(response.data);
        } catch (error) {
            console.log(error);
            alert("Unable to load proposal.");
        }
    };

    useEffect(() => {
        fetchProposal();
    }, []);

    const createPolicy = async () => {
        try {
            const policyData = {
                startDate: new Date().toISOString().split("T")[0],
                endDate: new Date(new Date().setFullYear(new Date().getFullYear() + 1)).toISOString().split("T")[0],
                policyStatus: "ACTIVE",
                policyNumber: "POL-" + proposalId
            };



            await axios.post(
                `http://localhost:8080/api/policy/add/${proposalId}`, policyData, config);
                alert("Policy Created Successfully");

            setTimeout(() => {
                navigate("/employee/proposals");
            }, 1000);

        } catch (error) {
            console.log(error);
            alert(error.response?.data || "Policy Creation Failed");
        }
    };

    if (!proposal) {
        return (
            <>
                <EmployeeNavbar />
                <div className="container py-5 text-center">
                    <div className="spinner-border text-primary"></div>
                </div>
            </>
        );
    }

    return (
        <div>
            <EmployeeNavbar />
            <div className="container py-4">
                <div className="card shadow">
                    <div className="card-header bg-primary text-white">
                        <h3 className="mb-0">Policy Preview</h3>
                    </div>
                    <div className="card-body">
                        <div className="row">
                            <div className="col-md-6 mb-3">
                                <label className="fw-bold">Customer Name</label>
                                <input className="form-control" value={proposal.policyHolderName} readOnly />
                            </div>
                            <div className="col-md-6 mb-3">
                                <label className="fw-bold">Vehicle Number</label>
                                <input className="form-control" value={proposal.vehicleNumber} readOnly />
                            </div>
                            <div className="col-md-6 mb-3">
                                <label className="fw-bold">Insurance Plan</label>
                                <input className="form-control" value={proposal.planType} readOnly />
                            </div>
                            <div className="col-md-6 mb-3">
                                <label className="fw-bold">Coverage Amount</label>
                                <input className="form-control" value={`₹ ${proposal.coverageAmount}`} readOnly />
                            </div>
                            <div className="col-md-4 mb-3">
                                <label className="fw-bold">Base Premium</label>
                                <input className="form-control" value={`₹ ${proposal.basePremium}`} readOnly />
                            </div>
                            <div className="col-md-4 mb-3">
                                <label className="fw-bold">Discount</label>
                                <input className="form-control" value={`₹ ${proposal.discount}`} readOnly />
                            </div>
                            <div className="col-md-4 mb-3">
                                <label className="fw-bold">Final Premium</label>
                                <input className="form-control" value={`₹ ${proposal.premiumAmount}`} readOnly />
                            </div>
                            <div className="col-md-6 mb-3">
                                <label className="fw-bold">Proposal Status</label>
                                <input className="form-control" value={proposal.proposalStatus} readOnly />
                            </div>
                            <div className="col-md-6 mb-3">
                                <label className="fw-bold">Proposal Date</label>
                                <input className="form-control" value={new Date(proposal.proposalDate).toLocaleDateString()} readOnly />
                            </div>
                        </div>
                        <hr />
                        <div className="d-flex justify-content-end gap-2">
                            <button className="btn btn-secondary" onClick={() => navigate(-1)}>
                                Back
                            </button>
                            <button className="btn btn-success px-4" onClick={createPolicy}>
                                Create Policy
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default EmployeePolicyManagement