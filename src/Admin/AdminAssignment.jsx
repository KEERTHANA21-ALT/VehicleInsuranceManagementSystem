
import { useState, useEffect } from "react";
import axios from "axios";
import AdminNavbar from "./AdminNavbar";

function AdminAssignment() {

    const [proposals, setProposals] = useState([]);
    const [claims, setClaims] = useState([]);
    const [employees, setEmployees] = useState([]);

    const [activeTab, setActiveTab] = useState("proposals");

    const [selectedProposalId, setSelectedProposalId] = useState("");
    const [selectedClaimId, setSelectedClaimId] = useState("");
    const [selectedEmployeeId, setSelectedEmployeeId] = useState("");

    const [proposalSearch, setProposalSearch] = useState("");
    const [claimSearch, setClaimSearch] = useState("");
    const [employeeSearch, setEmployeeSearch] = useState("");

    const token = localStorage.getItem("token");

    const config = {
        headers: {
            Authorization: "Bearer " + token
        }
    };


    // =========================================================
    // FETCH PROPOSALS
    // =========================================================

    const fetchProposals = async () => {

        try {

            const response = await axios.get(
                "http://localhost:8080/api/proposal/get-all",
                config
            );

            setProposals(response.data);

        } catch (error) {

            console.log("Proposal error:", error);

        }

    };


    // =========================================================
    // FETCH CLAIMS
    // =========================================================

    const fetchClaims = async () => {

        try {

            const response = await axios.get(
                "http://localhost:8080/api/claim/get-all",
                config
            );

            setClaims(response.data);

        } catch (error) {

            console.log("Claim error:", error);

        }

    };


    // =========================================================
    // FETCH EMPLOYEES
    // =========================================================

    const fetchEmployees = async () => {

        try {

            const response = await axios.get(
                "http://localhost:8080/api/employee/get-all",
                config
            );

            setEmployees(response.data);

        } catch (error) {

            console.log("Employee error:", error);

        }

    };


    // =========================================================
    // LOAD DATA
    // =========================================================

    useEffect(() => {

        fetchProposals();
        fetchClaims();
        fetchEmployees();

        const interval = setInterval(() => {

            fetchProposals();
            fetchClaims();
            fetchEmployees();

        }, 5000);

        return () => clearInterval(interval);

    }, []);


    // =========================================================
    // FILTER PROPOSALS
    // =========================================================

    const filteredProposals = proposals.filter(proposal => {

        return (
            proposal.employeeName === "Not Assigned" &&
            (
                proposal.policyHolderName
                    ?.toLowerCase()
                    .includes(proposalSearch.toLowerCase()) ||

                proposal.vehicleNumber
                    ?.toLowerCase()
                    .includes(proposalSearch.toLowerCase())
            )
        );

    });


    // =========================================================
    // FILTER CLAIMS
    // =========================================================

    const filteredClaims = claims.filter(claim => {

        return (
            (
                !claim.employeeName ||
                claim.employeeName === "Not Assigned"
            ) &&
            (
                String(claim.id)
                    .includes(claimSearch) ||

                claim.claimReason
                    ?.toLowerCase()
                    .includes(claimSearch.toLowerCase())
            )
        );

    });


    // =========================================================
    // FILTER MANAGERS
    // =========================================================

    const managers = employees.filter(emp => {

        return (
            emp.employeeRole === "INSURANCE_MANAGER" &&
            emp.name
                ?.toLowerCase()
                .includes(employeeSearch.toLowerCase())
        );

    });


    // =========================================================
    // FILTER SURVEYORS
    // =========================================================

    const surveyors = employees.filter(emp => {

        return (
            emp.employeeRole === "SURVEYOR" &&
            emp.name
                ?.toLowerCase()
                .includes(employeeSearch.toLowerCase())
        );

    });


    // =========================================================
    // ASSIGN PROPOSAL
    // =========================================================

    const assignProposal = async () => {

        if (!selectedProposalId || !selectedEmployeeId) {

            alert("Please select a proposal and insurance manager");

            return;

        }

        try {

            await axios.put(
                `http://localhost:8080/api/proposal/assign-employee/${selectedProposalId}/${selectedEmployeeId}`,
                {},
                config
            );

            alert("Proposal assigned successfully");

            setSelectedProposalId("");
            setSelectedEmployeeId("");
            setProposalSearch("");
            setEmployeeSearch("");

            fetchProposals();

        } catch (error) {

            console.log(error);

            alert("Proposal assignment failed");

        }

    };


    // =========================================================
    // ASSIGN CLAIM
    // =========================================================

    const assignClaim = async () => {

        if (!selectedClaimId || !selectedEmployeeId) {

            alert("Please select a claim and surveyor");

            return;

        }

        try {

            console.log("Claim ID:", selectedClaimId);
            console.log("Surveyor ID:", selectedEmployeeId);

            // IMPORTANT:
            // Backend API is assign-surveyor
            await axios.put(
                `http://localhost:8080/api/claim/assign-surveyor/${selectedClaimId}/${selectedEmployeeId}`,
                {},
                config
            );

            alert("Claim assigned to surveyor successfully");

            setSelectedClaimId("");
            setSelectedEmployeeId("");
            setClaimSearch("");
            setEmployeeSearch("");

            fetchClaims();

        } catch (error) {

            console.log("Claim assignment error:", error);

            alert("Claim assignment failed");

        }

    };


    // =========================================================
    // RESET
    // =========================================================

    const resetFields = () => {

        setSelectedProposalId("");
        setSelectedClaimId("");
        setSelectedEmployeeId("");

        setProposalSearch("");
        setClaimSearch("");
        setEmployeeSearch("");

    };


    // =========================================================
    // CHANGE TAB
    // =========================================================

    const changeTab = (tab) => {

        setActiveTab(tab);

        resetFields();

    };


    return (

        <div
            style={{
                minHeight: "100vh",
                backgroundColor: "#f5f7fb"
            }}
        >

            <AdminNavbar />

            <div className="container py-5">

                {/* HEADER */}

                <div className="mb-4">

                    <h2
                        className="fw-bold mb-1"
                        style={{
                            color: "#183b66"
                        }}
                    >
                        Assignment Management
                    </h2>

                    <p className="text-muted mb-0">
                        Assign proposals to insurance managers and claims to surveyors.
                    </p>

                </div>


                {/* TABS */}

                <div
                    className="bg-white p-2 shadow-sm mb-4"
                    style={{
                        borderRadius: "12px",
                        width: "fit-content"
                    }}
                >

                    <button
                        className="btn px-4 me-2"
                        onClick={() => changeTab("proposals")}
                        style={{
                            backgroundColor:
                                activeTab === "proposals"
                                    ? "#2563eb"
                                    : "transparent",

                            color:
                                activeTab === "proposals"
                                    ? "white"
                                    : "#475569",

                            borderRadius: "8px"
                        }}
                    >
                        <i className="bi bi-file-earmark-text me-2"></i>
                        Proposals
                    </button>


                    <button
                        className="btn px-4"
                        onClick={() => changeTab("claims")}
                        style={{
                            backgroundColor:
                                activeTab === "claims"
                                    ? "#0f766e"
                                    : "transparent",

                            color:
                                activeTab === "claims"
                                    ? "white"
                                    : "#475569",

                            borderRadius: "8px"
                        }}
                    >
                        <i className="bi bi-clipboard-check me-2"></i>
                        Claims
                    </button>

                </div>


                {/* =====================================================
                    PROPOSAL ASSIGNMENT
                ====================================================== */}

                {activeTab === "proposals" && (

                    <>

                        {/* ASSIGNMENT CARD */}

                        <div
                            className="card border-0 shadow-sm mb-4"
                            style={{
                                borderRadius: "14px"
                            }}
                        >

                            <div
                                className="card-body p-4"
                            >

                                <div className="d-flex align-items-center mb-4">

                                    <div
                                        className="me-3 d-flex align-items-center justify-content-center"
                                        style={{
                                            width: "42px",
                                            height: "42px",
                                            backgroundColor: "#e8f0ff",
                                            color: "#2563eb",
                                            borderRadius: "10px"
                                        }}
                                    >
                                        <i className="bi bi-person-check"></i>
                                    </div>

                                    <div>

                                        <h5 className="fw-bold mb-0">
                                            Assign Proposal
                                        </h5>

                                        <small className="text-muted">
                                            Assign a pending proposal to an insurance manager.
                                        </small>

                                    </div>

                                </div>


                                <div className="row g-3">


                                    {/* PROPOSAL */}

                                    <div className="col-md-5">

                                        <label className="form-label fw-semibold">
                                            Proposal
                                        </label>

                                        <select
                                            className="form-select"
                                            value={selectedProposalId}
                                            onChange={(e) => {

                                                setSelectedProposalId(
                                                    e.target.value
                                                );

                                                const proposal =
                                                    proposals.find(
                                                        p =>
                                                            String(p.id) ===
                                                            e.target.value
                                                    );

                                                if (proposal) {

                                                    setProposalSearch(
                                                        `${proposal.policyHolderName} - ${proposal.vehicleNumber}`
                                                    );

                                                }

                                            }}
                                        >

                                            <option value="">
                                                Select Proposal
                                            </option>

                                            {filteredProposals.map(
                                                proposal => (

                                                    <option
                                                        key={proposal.id}
                                                        value={proposal.id}
                                                    >
                                                        {proposal.policyHolderName}
                                                        {" - "}
                                                        {proposal.vehicleNumber}
                                                    </option>

                                                )
                                            )}

                                        </select>

                                    </div>


                                    {/* MANAGER */}

                                    <div className="col-md-5">

                                        <label className="form-label fw-semibold">
                                            Insurance Manager
                                        </label>

                                        <select
                                            className="form-select"
                                            value={selectedEmployeeId}
                                            onChange={(e) => {

                                                setSelectedEmployeeId(
                                                    e.target.value
                                                );

                                            }}
                                        >

                                            <option value="">
                                                Select Manager
                                            </option>

                                            {managers.map(emp => (

                                                <option
                                                    key={emp.id}
                                                    value={emp.id}
                                                >
                                                    {emp.name}
                                                </option>

                                            ))}

                                        </select>

                                    </div>


                                    {/* BUTTON */}

                                    <div className="col-md-2 d-flex align-items-end">

                                        <button
                                            className="btn w-100"
                                            onClick={assignProposal}
                                            style={{
                                                backgroundColor: "#2563eb",
                                                color: "white",
                                                borderRadius: "8px"
                                            }}
                                        >
                                            Assign
                                        </button>

                                    </div>

                                </div>

                            </div>

                        </div>


                        {/* PROPOSALS */}

                        <div
                            className="card border-0 shadow-sm"
                            style={{
                                borderRadius: "14px"
                            }}
                        >

                            <div className="card-body p-4">

                                <div className="d-flex justify-content-between align-items-center mb-4">

                                    <div>

                                        <h5 className="fw-bold mb-1">
                                            Proposals
                                        </h5>

                                        <small className="text-muted">
                                            All proposal assignments
                                        </small>

                                    </div>

                                    <span
                                        className="badge px-3 py-2"
                                        style={{
                                            backgroundColor: "#e8f0ff",
                                            color: "#2563eb"
                                        }}
                                    >
                                        {proposals.length} Proposals
                                    </span>

                                </div>


                                <div className="table-responsive">

                                    <table className="table align-middle">

                                        <thead>

                                            <tr>

                                                <th>S.No</th>
                                                <th>Customer</th>
                                                <th>Vehicle</th>
                                                <th>Plan</th>
                                                <th>Premium</th>
                                                <th>Status</th>
                                                <th>Manager</th>

                                            </tr>

                                        </thead>


                                        <tbody>

                                            {proposals.map(
                                                (proposal, index) => (

                                                    <tr key={proposal.id}>

                                                        <td>
                                                            {index + 1}
                                                        </td>

                                                        <td className="fw-semibold">
                                                            {
                                                                proposal.policyHolderName
                                                            }
                                                        </td>

                                                        <td>
                                                            {
                                                                proposal.vehicleNumber
                                                            }
                                                        </td>

                                                        <td>
                                                            {
                                                                proposal.planType
                                                            }
                                                        </td>

                                                        <td>
                                                            ₹ {proposal.premiumAmount}
                                                        </td>

                                                        <td>

                                                            <span
                                                                className="badge px-3 py-2"
                                                                style={{
                                                                    backgroundColor:
                                                                        proposal.proposalStatus === "APPROVED"
                                                                            ? "#dcfce7"
                                                                            : "#e0e7ff",

                                                                    color:
                                                                        proposal.proposalStatus === "APPROVED"
                                                                            ? "#15803d"
                                                                            : "#4338ca"
                                                                }}
                                                            >
                                                                {
                                                                    proposal.proposalStatus
                                                                }
                                                            </span>

                                                        </td>

                                                        <td>

                                                            {proposal.employeeName ===
                                                            "Not Assigned" ? (

                                                                <span
                                                                    className="badge px-3 py-2"
                                                                    style={{
                                                                        backgroundColor: "#f1f5f9",
                                                                        color: "#64748b"
                                                                    }}
                                                                >
                                                                    Unassigned
                                                                </span>

                                                            ) : (

                                                                <span
                                                                    className="badge px-3 py-2"
                                                                    style={{
                                                                        backgroundColor: "#dcfce7",
                                                                        color: "#15803d"
                                                                    }}
                                                                >
                                                                    {
                                                                        proposal.employeeName
                                                                    }
                                                                </span>

                                                            )}

                                                        </td>

                                                    </tr>

                                                )
                                            )}

                                        </tbody>

                                    </table>

                                </div>

                            </div>

                        </div>

                    </>

                )}


                {/* =====================================================
                    CLAIM ASSIGNMENT
                ====================================================== */}

                {activeTab === "claims" && (

                    <>

                        {/* ASSIGN CLAIM */}

                        <div
                            className="card border-0 shadow-sm mb-4"
                            style={{
                                borderRadius: "14px"
                            }}
                        >

                            <div className="card-body p-4">

                                <div className="d-flex align-items-center mb-4">

                                    <div
                                        className="me-3 d-flex align-items-center justify-content-center"
                                        style={{
                                            width: "42px",
                                            height: "42px",
                                            backgroundColor: "#e6fffb",
                                            color: "#0f766e",
                                            borderRadius: "10px"
                                        }}
                                    >
                                        <i className="bi bi-person-check"></i>
                                    </div>

                                    <div>

                                        <h5 className="fw-bold mb-0">
                                            Assign Claim
                                        </h5>

                                        <small className="text-muted">
                                            Assign a pending claim to a surveyor.
                                        </small>

                                    </div>

                                </div>


                                <div className="row g-3">


                                    {/* CLAIM */}

                                    <div className="col-md-5">

                                        <label className="form-label fw-semibold">
                                            Claim
                                        </label>

                                        <select
                                            className="form-select"
                                            value={selectedClaimId}
                                            onChange={(e) => {

                                                setSelectedClaimId(
                                                    e.target.value
                                                );

                                            }}
                                        >

                                            <option value="">
                                                Select Claim
                                            </option>

                                            {filteredClaims.map(
                                                claim => (

                                                    <option
                                                        key={claim.id}
                                                        value={claim.id}
                                                    >
                                                        {claim.id}
                                                        {" - "}
                                                        {claim.claimReason}
                                                    </option>

                                                )
                                            )}

                                        </select>

                                    </div>


                                    {/* SURVEYOR */}

                                    <div className="col-md-5">

                                        <label className="form-label fw-semibold">
                                            Surveyor
                                        </label>

                                        <select
                                            className="form-select"
                                            value={selectedEmployeeId}
                                            onChange={(e) => {

                                                setSelectedEmployeeId(
                                                    e.target.value
                                                );

                                            }}
                                        >

                                            <option value="">
                                                Select Surveyor
                                            </option>

                                            {surveyors.map(emp => (

                                                <option
                                                    key={emp.id}
                                                    value={emp.id}
                                                >
                                                    {emp.name}
                                                </option>

                                            ))}

                                        </select>

                                    </div>


                                    {/* BUTTON */}

                                    <div className="col-md-2 d-flex align-items-end">

                                        <button
                                            className="btn w-100"
                                            onClick={assignClaim}
                                            style={{
                                                backgroundColor: "#0f766e",
                                                color: "white",
                                                borderRadius: "8px"
                                            }}
                                        >
                                            Assign
                                        </button>

                                    </div>

                                </div>

                            </div>

                        </div>


                        {/* CLAIM TABLE */}

                        <div
                            className="card border-0 shadow-sm"
                            style={{
                                borderRadius: "14px"
                            }}
                        >

                            <div className="card-body p-4">

                                <div className="d-flex justify-content-between align-items-center mb-4">

                                    <div>

                                        <h5 className="fw-bold mb-1">
                                            Claims
                                        </h5>

                                        <small className="text-muted">
                                            Track claim assignment and status
                                        </small>

                                    </div>

                                    <span
                                        className="badge px-3 py-2"
                                        style={{
                                            backgroundColor: "#e6fffb",
                                            color: "#0f766e"
                                        }}
                                    >
                                        {claims.length} Claims
                                    </span>

                                </div>


                                <div className="table-responsive">

                                    <table className="table align-middle">

                                        <thead>

                                            <tr>

                                                <th>S.No</th>
                                                <th>Claim ID</th>
                                                <th>Reason</th>
                                                <th>Description</th>
                                                <th>Status</th>
                                                <th>Surveyor</th>

                                            </tr>

                                        </thead>


                                        <tbody>

                                            {claims.map(
                                                (claim, index) => (

                                                    <tr key={claim.id}>

                                                        <td>
                                                            {index + 1}
                                                        </td>

                                                        <td className="fw-semibold">
                                                            {claim.id}
                                                        </td>

                                                        <td>

                                                            <span
                                                                className="badge px-3 py-2"
                                                                style={{
                                                                    backgroundColor: "#f1f5f9",
                                                                    color: "#334155"
                                                                }}
                                                            >
                                                                {
                                                                    claim.claimReason
                                                                }
                                                            </span>

                                                        </td>

                                                        <td
                                                            style={{
                                                                maxWidth: "300px"
                                                            }}
                                                        >
                                                            <span
                                                                className="text-muted"
                                                            >
                                                                {
                                                                    claim.claimRemarks
                                                                }
                                                            </span>
                                                        </td>

                                                        <td>

                                                            <span
                                                                className="badge px-3 py-2"
                                                                style={{
                                                                    backgroundColor:
                                                                        claim.claimStatus === "APPROVED"
                                                                            ? "#dcfce7"
                                                                            : claim.claimStatus === "REJECTED"
                                                                                ? "#fee2e2"
                                                                                : claim.claimStatus === "UNDER_REVIEW"
                                                                                    ? "#dbeafe"
                                                                                    : "#f1f5f9",

                                                                    color:
                                                                        claim.claimStatus === "APPROVED"
                                                                            ? "#15803d"
                                                                            : claim.claimStatus === "REJECTED"
                                                                                ? "#b91c1c"
                                                                                : claim.claimStatus === "UNDER_REVIEW"
                                                                                    ? "#1d4ed8"
                                                                                    : "#475569"
                                                                }}
                                                            >
                                                                {
                                                                    claim.claimStatus ||
                                                                    "SUBMITTED"
                                                                }
                                                            </span>

                                                        </td>

                                                        <td>

                                                            {claim.employeeName ? (

                                                                <span
                                                                    className="badge px-3 py-2"
                                                                    style={{
                                                                        backgroundColor: "#dcfce7",
                                                                        color: "#15803d"
                                                                    }}
                                                                >
                                                                    {
                                                                        claim.employeeName
                                                                    }
                                                                </span>

                                                            ) : (

                                                                <span
                                                                    className="badge px-3 py-2"
                                                                    style={{
                                                                        backgroundColor: "#f1f5f9",
                                                                        color: "#64748b"
                                                                    }}
                                                                >
                                                                    Unassigned
                                                                </span>

                                                            )}

                                                        </td>

                                                    </tr>

                                                )
                                            )}

                                        </tbody>

                                    </table>

                                </div>

                            </div>

                        </div>

                    </>

                )}

            </div>

        </div>

    );

}

export default AdminAssignment