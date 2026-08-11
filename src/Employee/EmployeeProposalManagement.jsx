
import { useEffect, useState } from "react";
import axios from "axios";
import EmployeeNavbar from "./EmployeeNavbar";
import { useNavigate } from "react-router";

function EmployeeProposalManagement() {

    const navigate = useNavigate();

    const [proposals, setProposals] = useState([]);
    const [loading, setLoading] = useState(true);
    const [filter, setFilter] = useState("ALL");

    const token = localStorage.getItem("token");

    const config = {
        headers: {
            Authorization: "Bearer " + token
        }
    };


    // =====================================================
    // FETCH PROPOSALS
    // =====================================================

    const fetchProposals = async () => {

        try {

            const response = await axios.get(
                "http://localhost:8080/api/proposal/get-employeeProposals",
                config
            );

            const proposalList = Array.isArray(response.data)
                ? response.data
                : [];


            // Check payment status for approved proposals
            const updatedProposals = await Promise.all(

                proposalList.map(async (proposal) => {

                    if (proposal.proposalStatus === "APPROVED") {

                        try {

                            const paymentResponse = await axios.get(
                                `http://localhost:8080/api/payment/get-ByProposalId/${proposal.id}`,
                                config
                            );

                            const payments = Array.isArray(
                                paymentResponse.data
                            )
                                ? paymentResponse.data
                                : [];

                            const successfulPayment = payments.find(
                                payment =>
                                    payment.paymentStatus === "SUCCESS"
                            );

                            return {
                                ...proposal,
                                paymentCompleted:
                                    !!successfulPayment
                            };

                        } catch (error) {

                            return {
                                ...proposal,
                                paymentCompleted: false
                            };

                        }

                    }

                    return {
                        ...proposal,
                        paymentCompleted: false
                    };

                })

            );

            setProposals(updatedProposals);

        } catch (error) {

            console.log(error);

        } finally {

            setLoading(false);

        }

    };


    // =====================================================
    // INITIAL LOAD
    // =====================================================

    useEffect(() => {

        fetchProposals();

        // Check payment/policy changes periodically
        const interval = setInterval(() => {
            fetchProposals();
        }, 10000);

        return () => clearInterval(interval);

    }, []);


    // =====================================================
    // UPDATE PROPOSAL STATUS
    // =====================================================

    const updateStatus = async (id, status) => {

        try {

            await axios.put(
                `http://localhost:8080/api/proposal/update/${id}`,
                {
                    proposalStatus: status
                },
                config
            );

            await fetchProposals();

        } catch (error) {

            console.log(error);
            alert("Unable to update proposal status.");

        }

    };


    // =====================================================
    // GET PROCESS STATUS
    // =====================================================

    const getProcessStatus = (proposal) => {

        if (proposal.proposalStatus === "REJECTED") {
            return "REJECTED";
        }

        if (proposal.policyCreated) {
            return "POLICY_GENERATED";
        }

        if (
            proposal.proposalStatus === "APPROVED" &&
            proposal.paymentCompleted
        ) {
            return "READY_FOR_POLICY";
        }

        if (proposal.proposalStatus === "APPROVED") {
            return "PAYMENT_PENDING";
        }

        return "PENDING_REVIEW";

    };


    // =====================================================
    // FILTER
    // =====================================================

    const filteredProposals = proposals.filter((proposal) => {

        const processStatus = getProcessStatus(proposal);

        if (filter === "ALL") {
            return true;
        }

        if (filter === "PENDING_REVIEW") {
            return processStatus === "PENDING_REVIEW";
        }

        if (filter === "APPROVED") {
            return proposal.proposalStatus === "APPROVED";
        }

        if (filter === "PAYMENT_PENDING") {
            return processStatus === "PAYMENT_PENDING";
        }

        if (filter === "READY_FOR_POLICY") {
            return processStatus === "READY_FOR_POLICY";
        }

        if (filter === "POLICY_GENERATED") {
            return processStatus === "POLICY_GENERATED";
        }

        if (filter === "REJECTED") {
            return processStatus === "REJECTED";
        }

        return true;

    });


    // =====================================================
    // COUNTS
    // =====================================================

    const getCount = (type) => {

        return proposals.filter((proposal) => {

            return getProcessStatus(proposal) === type;

        }).length;

    };


    // =====================================================
    // STATUS STYLE
    // =====================================================

    const getStatusStyle = (status) => {

        const styles = {

            PROPOSAL_SUBMITTED: {
                background: "#eef5ff",
                color: "#1261a0"
            },

            INSPECTION_PENDING: {
                background: "#fff6df",
                color: "#a66a00"
            },

            UNDER_REVIEW: {
                background: "#f0efff",
                color: "#5146a5"
            },

            QUOTE_GENERATED: {
                background: "#f4edff",
                color: "#7048b8"
            },

            APPROVED: {
                background: "#e9f7ef",
                color: "#198754"
            },

            REJECTED: {
                background: "#fdecec",
                color: "#dc3545"
            }

        };

        return styles[status] || {
            background: "#f1f3f5",
            color: "#6c757d"
        };

    };


    // =====================================================
    // STATUS LABEL
    // =====================================================

    const getStatusLabel = (status) => {

        const labels = {

            PROPOSAL_SUBMITTED: "Proposal Submitted",
            INSPECTION_PENDING: "Inspection Pending",
            UNDER_REVIEW: "Under Review",
            QUOTE_GENERATED: "Quote Generated",
            APPROVED: "Approved",
            REJECTED: "Rejected"

        };

        return labels[status] || status;

    };


    // =====================================================
    // FORMAT MONEY
    // =====================================================

    const formatMoney = (amount) => {

        return Number(amount || 0).toLocaleString(
            "en-IN",
            {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2
            }
        );

    };


    // =====================================================
    // LOADING
    // =====================================================

    if (loading) {

        return (

            <div
                className="min-vh-100"
                style={{
                    backgroundColor: "#f5f7fa"
                }}
            >

                <EmployeeNavbar />

                <div className="container py-5 text-center">

                    <div
                        className="spinner-border"
                        style={{
                            color: "#1261a0"
                        }}
                    />

                    <p className="text-muted mt-3">
                        Loading proposals...
                    </p>

                </div>

            </div>

        );

    }


    // =====================================================
    // UI
    // =====================================================

    return (

        <div
            className="min-vh-100"
            style={{
                backgroundColor: "#f5f7fa"
            }}
        >

            <EmployeeNavbar />


            <div className="container py-4 py-lg-5">


                {/* =================================================
                    HEADER
                ================================================= */}

                <div className="mb-4">

                    <h2
                        className="fw-bold mb-1"
                        style={{
                            color: "#123b6d"
                        }}
                    >
                        Assigned Proposals
                    </h2>

                    <p className="text-muted mb-0">
                        Manage assigned proposals, payments and policies.
                    </p>

                </div>


                {/* =================================================
                    SUMMARY
                ================================================= */}

                <div className="row g-3 mb-4">

                    <div className="col-md-3">

                        <div
                            className="bg-white p-3 h-100"
                            style={{
                                borderRadius: "12px",
                                border: "1px solid #e5eaf0"
                            }}
                        >

                            <small className="text-muted">
                                Total Proposals
                            </small>

                            <h4
                                className="fw-bold mb-0 mt-1"
                                style={{
                                    color: "#123b6d"
                                }}
                            >
                                {proposals.length}
                            </h4>

                        </div>

                    </div>


                    <div className="col-md-3">

                        <div
                            className="bg-white p-3 h-100"
                            style={{
                                borderRadius: "12px",
                                border: "1px solid #e5eaf0"
                            }}
                        >

                            <small className="text-muted">
                                Payment Pending
                            </small>

                            <h4
                                className="fw-bold mb-0 mt-1"
                                style={{
                                    color: "#a66a00"
                                }}
                            >
                                {getCount("PAYMENT_PENDING")}
                            </h4>

                        </div>

                    </div>


                    <div className="col-md-3">

                        <div
                            className="bg-white p-3 h-100"
                            style={{
                                borderRadius: "12px",
                                border: "1px solid #e5eaf0"
                            }}
                        >

                            <small className="text-muted">
                                Ready for Policy
                            </small>

                            <h4
                                className="fw-bold mb-0 mt-1"
                                style={{
                                    color: "#198754"
                                }}
                            >
                                {getCount("READY_FOR_POLICY")}
                            </h4>

                        </div>

                    </div>


                    <div className="col-md-3">

                        <div
                            className="bg-white p-3 h-100"
                            style={{
                                borderRadius: "12px",
                                border: "1px solid #e5eaf0"
                            }}
                        >

                            <small className="text-muted">
                                Policies Generated
                            </small>

                            <h4
                                className="fw-bold mb-0 mt-1"
                                style={{
                                    color: "#1261a0"
                                }}
                            >
                                {getCount("POLICY_GENERATED")}
                            </h4>

                        </div>

                    </div>

                </div>


                {/* =================================================
                    FILTER
                ================================================= */}

                <div
                    className="bg-white p-3 mb-4"
                    style={{
                        borderRadius: "12px",
                        border: "1px solid #e5eaf0"
                    }}
                >

                    <div className="d-flex flex-wrap align-items-center gap-2">

                        <span
                            className="fw-semibold me-2"
                            style={{
                                color: "#123b6d"
                            }}
                        >
                            Filter:
                        </span>


                        {[
                            ["ALL", "All"],
                            ["PENDING_REVIEW", "Pending Review"],
                            ["PAYMENT_PENDING", "Payment Pending"],
                            ["READY_FOR_POLICY", "Ready for Policy"],
                            ["POLICY_GENERATED", "Policy Generated"],
                            ["REJECTED", "Rejected"]
                        ].map(([value, label]) => (

                            <button
                                key={value}
                                type="button"
                                onClick={() => setFilter(value)}
                                className="btn btn-sm"
                                style={{
                                    backgroundColor:
                                        filter === value
                                            ? "#1261a0"
                                            : "#f3f6f9",

                                    color:
                                        filter === value
                                            ? "#ffffff"
                                            : "#52606d",

                                    border:
                                        "1px solid " +
                                        (
                                            filter === value
                                                ? "#1261a0"
                                                : "#e0e6ec"
                                        ),

                                    borderRadius: "7px",
                                    padding: "7px 14px"
                                }}
                            >
                                {label}
                            </button>

                        ))}

                    </div>

                </div>


                {/* =================================================
                    PROPOSALS
                ================================================= */}

                {filteredProposals.length === 0 ? (

                    <div
                        className="bg-white text-center"
                        style={{
                            borderRadius: "14px",
                            border: "1px solid #e5eaf0",
                            padding: "60px 20px"
                        }}
                    >

                        <h5
                            className="fw-bold mb-2"
                            style={{
                                color: "#123b6d"
                            }}
                        >
                            No Proposals Found
                        </h5>

                        <p className="text-muted mb-0">
                            There are no proposals matching this filter.
                        </p>

                    </div>

                ) : (

                    <div className="d-flex flex-column gap-3">

                        {filteredProposals.map((proposal) => {

                            const status =
                                proposal.proposalStatus;

                            const statusStyle =
                                getStatusStyle(status);

                            const processStatus =
                                getProcessStatus(proposal);

                            const isApproved =
                                status === "APPROVED";

                            const isRejected =
                                status === "REJECTED";

                            const isPaid =
                                proposal.paymentCompleted === true;

                            const isPolicyCreated =
                                proposal.policyCreated === true;


                            return (

                                <div
                                    key={proposal.id}
                                    className="bg-white"
                                    style={{
                                        borderRadius: "14px",
                                        border: "1px solid #e4e9ef",
                                        boxShadow:
                                            "0 2px 10px rgba(30, 55, 80, 0.04)"
                                    }}
                                >

                                    <div className="p-4">


                                        {/* =================================================
                                            HEADER
                                        ================================================= */}

                                        <div className="d-flex justify-content-between align-items-start">

                                            <div>

                                                <small
                                                    className="text-uppercase fw-semibold"
                                                    style={{
                                                        color: "#8a96a3",
                                                        fontSize: "11px",
                                                        letterSpacing: "0.6px"
                                                    }}
                                                >
                                                    Insurance Proposal
                                                </small>

                                                <h5
                                                    className="fw-bold mb-1 mt-1"
                                                    style={{
                                                        color: "#123b6d"
                                                    }}
                                                >
                                                    Proposal #{proposal.id}
                                                </h5>

                                                <small className="text-muted">
                                                    {proposal.policyHolderName}
                                                </small>

                                            </div>


                                            <span
                                                className="fw-semibold"
                                                style={{
                                                    backgroundColor:
                                                        statusStyle.background,

                                                    color:
                                                        statusStyle.color,

                                                    padding: "7px 14px",

                                                    borderRadius: "20px",

                                                    fontSize: "12px"
                                                }}
                                            >
                                                {getStatusLabel(status)}
                                            </span>

                                        </div>


                                        {/* =================================================
                                            DETAILS
                                        ================================================= */}

                                        <div
                                            className="row mt-4 pt-3"
                                            style={{
                                                borderTop:
                                                    "1px solid #edf0f4"
                                            }}
                                        >

                                            <div className="col-md-3">

                                                <small className="text-muted">
                                                    Vehicle
                                                </small>

                                                <div className="fw-semibold mt-1">
                                                    {proposal.vehicleNumber}
                                                </div>

                                            </div>


                                            <div className="col-md-3">

                                                <small className="text-muted">
                                                    Insurance Plan
                                                </small>

                                                <div className="fw-semibold mt-1">
                                                    {proposal.planType}
                                                </div>

                                            </div>


                                            <div className="col-md-3">

                                                <small className="text-muted">
                                                    Premium
                                                </small>

                                                <div
                                                    className="fw-bold mt-1"
                                                    style={{
                                                        color: "#1261a0"
                                                    }}
                                                >
                                                    ₹ {formatMoney(
                                                        proposal.premiumAmount
                                                    )}
                                                </div>

                                            </div>


                                            <div className="col-md-3">

                                                <small className="text-muted">
                                                    Process Status
                                                </small>

                                                <div className="fw-semibold mt-1">

                                                    {processStatus === "PAYMENT_PENDING" &&
                                                        <span style={{ color: "#a66a00" }}>
                                                            Payment Pending
                                                        </span>
                                                    }

                                                    {processStatus === "READY_FOR_POLICY" &&
                                                        <span style={{ color: "#198754" }}>
                                                            Ready for Policy
                                                        </span>
                                                    }

                                                    {processStatus === "POLICY_GENERATED" &&
                                                        <span style={{ color: "#198754" }}>
                                                            Policy Generated
                                                        </span>
                                                    }

                                                    {processStatus === "PENDING_REVIEW" &&
                                                        <span style={{ color: "#52606d" }}>
                                                            Pending Review
                                                        </span>
                                                    }

                                                    {processStatus === "REJECTED" &&
                                                        <span style={{ color: "#dc3545" }}>
                                                            Rejected
                                                        </span>
                                                    }

                                                </div>

                                            </div>

                                        </div>


                                        {/* =================================================
                                            ACTION AREA
                                        ================================================= */}

                                        <div
                                            className="d-flex justify-content-between align-items-center mt-4 pt-3"
                                            style={{
                                                borderTop:
                                                    "1px solid #edf0f4"
                                            }}
                                        >

                                            {/* LEFT MESSAGE */}

                                            <div>

                                                {isRejected && (

                                                    <small
                                                        className="fw-semibold"
                                                        style={{
                                                            color: "#dc3545"
                                                        }}
                                                    >
                                                        Proposal rejected
                                                    </small>

                                                )}


                                                {!isRejected &&
                                                    !isApproved && (

                                                        <small className="text-muted">
                                                            Waiting for proposal approval
                                                        </small>

                                                    )}


                                                {isApproved &&
                                                    !isPaid &&
                                                    !isPolicyCreated && (

                                                        <small
                                                            className="fw-semibold"
                                                            style={{
                                                                color: "#a66a00"
                                                            }}
                                                        >
                                                            Waiting for customer payment
                                                        </small>

                                                    )}


                                                {isPaid &&
                                                    !isPolicyCreated && (

                                                        <small
                                                            className="fw-semibold"
                                                            style={{
                                                                color: "#198754"
                                                            }}
                                                        >
                                                            Payment successful — policy can now be generated
                                                        </small>

                                                    )}


                                                {isPolicyCreated && (

                                                    <small
                                                        className="fw-semibold"
                                                        style={{
                                                            color: "#198754"
                                                        }}
                                                    >
                                                        Policy successfully generated
                                                    </small>

                                                )}

                                            </div>


                                            {/* RIGHT ACTION */}

                                            <div>

                                                {!isApproved && !isRejected && (

                                                    <select
                                                        className="form-select"
                                                        value={status}
                                                        onChange={(e) =>
                                                            updateStatus(
                                                                proposal.id,
                                                                e.target.value
                                                            )
                                                        }
                                                        style={{
                                                            width: "200px",
                                                            fontSize: "13px"
                                                        }}
                                                    >

                                                        <option value="PROPOSAL_SUBMITTED">
                                                            Proposal Submitted
                                                        </option>

                                                        <option value="INSPECTION_PENDING">
                                                            Inspection Pending
                                                        </option>

                                                        <option value="UNDER_REVIEW">
                                                            Under Review
                                                        </option>

                                                        <option value="QUOTE_GENERATED">
                                                            Quote Generated
                                                        </option>

                                                        <option value="APPROVED">
                                                            Approved
                                                        </option>

                                                        <option value="REJECTED">
                                                            Rejected
                                                        </option>

                                                    </select>

                                                )}


                                                {isApproved &&
                                                    !isPaid &&
                                                    !isPolicyCreated && (

                                                        <span
                                                            className="badge"
                                                            style={{
                                                                backgroundColor: "#fff4df",
                                                                color: "#a66a00",
                                                                padding: "9px 14px"
                                                            }}
                                                        >
                                                            Payment Pending
                                                        </span>

                                                    )}


                                                {isPaid &&
                                                    !isPolicyCreated && (

                                                        <button
                                                            className="btn btn-success px-4"
                                                            onClick={() =>
                                                                navigate(
                                                                    `/employee/policy/${proposal.id}`
                                                                )
                                                            }
                                                        >

                                                            <i className="bi bi-file-earmark-check me-2"></i>

                                                            Generate Policy

                                                        </button>

                                                    )}


                                                {isPolicyCreated && (

                                                    <span
                                                        className="d-flex align-items-center gap-2 fw-semibold"
                                                        style={{
                                                            color: "#198754"
                                                        }}
                                                    >

                                                        <i className="bi bi-shield-check"></i>

                                                        Policy Generated

                                                    </span>

                                                )}

                                            </div>

                                        </div>

                                    </div>

                                </div>

                            );

                        })}

                    </div>

                )}

            </div>

        </div>

    );

}

export default EmployeeProposalManagement;

