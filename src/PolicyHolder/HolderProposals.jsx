import { useEffect, useState } from "react"
import axios from "axios"
import { useNavigate } from "react-router"
import HolderNavbar from "./HolderNavbar"

function HolderProposals() {
    const [proposals, setProposals] = useState([])
    const [loading, setLoading] = useState(true)

    const navigate = useNavigate()
    const token = localStorage.getItem("token")

    const config = {
        headers: {
            Authorization: "Bearer " + token,
        },
    }

    // Fetch proposals and their corresponding payment statuses
    useEffect(() => {
        const getProposals = async () => {
            try {
                const response = await axios.get(
                    "http://localhost:8080/api/proposal/get-myProposals",
                    config
                )

                const proposalList = response.data

                // Fetch payment details concurrently using Promise.all
                const updatedProposals = await Promise.all(
                    proposalList.map(async (proposal) => {
                        let paymentCompleted = false
                        let paymentPending = false

                        try {
                            const paymentResponse = await axios.get(
                                `http://localhost:8080/api/payment/get-ByProposalId/${proposal.id}`,
                                config
                            )

                            const payments = paymentResponse.data

                            for (let j = 0; j < payments.length; j++) {
                                if (payments[j].paymentStatus === "SUCCESS") {
                                    paymentCompleted = true
                                }
                                if (payments[j].paymentStatus === "PENDING") {
                                    paymentPending = true
                                }
                            }
                        } catch (error) {
                            console.error(`Payment error for proposal #${proposal.id}`, error)
                        }

                        return {
                            ...proposal,
                            paymentCompleted,
                            paymentPending,
                        }
                    })
                )

                setProposals(updatedProposals)
            } catch (error) {
                console.error("Proposal error:", error)
            } finally {
                setLoading(false)
            }
        }

        getProposals()
    }, [])

    // Get status text label
    const getStatus = (proposal) => {
        if (proposal.proposalStatus === "APPROVED") {
            if (proposal.paymentCompleted === true) {
                return "Payment Completed"
            }
            if (proposal.paymentPending === true) {
                return "Payment Pending"
            }
            return "Approved"
        }

        switch (proposal.proposalStatus) {
            case "REJECTED":
                return "Rejected"
            case "PROPOSAL_SUBMITTED":
                return "Proposal Submitted"
            case "INSPECTION_PENDING":
                return "Inspection Pending"
            case "UNDER_REVIEW":
                return "Under Review"
            case "QUOTE_GENERATED":
                return "Quote Generated"
            case "POLICY_ISSUED":
                return "Policy Issued"
            default:
                return "Unknown"
        }
    }

    // Get percentage progress
    const getProgress = (proposal) => {
        if (proposal.proposalStatus === "APPROVED") {
            if (proposal.paymentCompleted === true) {
                return 100
            }
            if (proposal.paymentPending === true) {
                return 90
            }
            return 80
        }

        switch (proposal.proposalStatus) {
            case "REJECTED":
                return 100
            case "PROPOSAL_SUBMITTED":
                return 15
            case "INSPECTION_PENDING":
                return 30
            case "UNDER_REVIEW":
                return 50
            case "QUOTE_GENERATED":
                return 65
            case "POLICY_ISSUED":
                return 100
            default:
                return 0
        }
    }

    // Get color depending on state
    const getStatusColor = (proposal) => {
        if (proposal.proposalStatus === "APPROVED") {
            if (proposal.paymentCompleted === true) {
                return "#198754"
            }
            if (proposal.paymentPending === true) {
                return "#a66a00"
            }
            return "#198754"
        }

        switch (proposal.proposalStatus) {
            case "REJECTED":
                return "#dc3545"
            case "INSPECTION_PENDING":
                return "#a66a00"
            case "UNDER_REVIEW":
                return "#4f46a5"
            case "QUOTE_GENERATED":
                return "#7048b8"
            case "PROPOSAL_SUBMITTED":
                return "#1261a0"
            case "POLICY_ISSUED":
                return "#198754"
            default:
                return "#1261a0"
        }
    }

    // Navigate to payment page
    const handlePayment = (id) => {
        navigate("/holder/payment/" + id)
    }

    // Render Loader UI
    if (loading) {
        return (
            <div className="min-vh-100" style={{ backgroundColor: "#f5f7fa" }}>
                <HolderNavbar />
                <div className="container py-5 text-center">
                    <div
                        className="spinner-border"
                        style={{ color: "#1261a0" }}
                        role="status"
                    />
                    <p className="text-muted mt-3">Loading your proposals...</p>
                </div>
            </div>
        )
    }

    // Render Component UI
    return (
        <div className="min-vh-100" style={{ backgroundColor: "#f5f7fa" }}>
            <HolderNavbar />

            <div className="container py-4 py-md-5">
                {/* HEADER */}
                <div className="d-flex justify-content-between align-items-center mb-4">
                    <div>
                        <h2 className="fw-bold mb-1" style={{ color: "#123b6d" }}>
                            My Proposals
                        </h2>
                        <p className="text-muted mb-0">
                            View and track your vehicle insurance proposals.
                        </p>
                    </div>

                    <div
                        className="bg-white shadow-sm text-center"
                        style={{
                            minWidth: "130px",
                            borderRadius: "10px",
                            padding: "12px 20px",
                            border: "1px solid #edf0f4",
                        }}
                    >
                        <small className="text-muted d-block">Total Proposals</small>
                        <div className="fw-bold fs-4 mt-1" style={{ color: "#1261a0" }}>
                            {proposals.length}
                        </div>
                    </div>
                </div>

                {/* NO PROPOSALS VIEW */}
                {proposals.length === 0 ? (
                    <div
                        className="bg-white text-center"
                        style={{
                            borderRadius: "14px",
                            border: "1px solid #e8edf2",
                            padding: "60px 20px",
                        }}
                    >
                        <h5 className="fw-bold mb-2" style={{ color: "#123b6d" }}>
                            No Proposals Yet
                        </h5>
                        <p className="text-muted mb-0">
                            Your insurance proposals will appear here once created.
                        </p>
                    </div>
                ) : (
                    /* PROPOSALS LIST VIEW */
                    <div className="row g-4">
                        {proposals.map((proposal) => (
                            <div className="col-xl-6" key={proposal.id}>
                                <div
                                    className="bg-white h-100"
                                    style={{
                                        borderRadius: "14px",
                                        border: "1px solid #e6ebf0",
                                        boxShadow: "0 3px 12px rgba(20, 50, 80, 0.05)",
                                    }}
                                >
                                    <div className="p-4">
                                        {/* PROPOSAL HEADER */}
                                        <div className="d-flex justify-content-between align-items-start">
                                            <div>
                                                <small
                                                    className="text-uppercase fw-semibold"
                                                    style={{ color: "#7a8794" }}
                                                >
                                                    Insurance Proposal
                                                </small>
                                                <h5
                                                    className="fw-bold mb-0"
                                                    style={{ color: "#123b6d" }}
                                                >
                                                    Proposal #{proposal.id}
                                                </h5>
                                            </div>

                                            <span
                                                className="fw-semibold"
                                                style={{
                                                    backgroundColor: "#f1f3f5",
                                                    color: getStatusColor(proposal),
                                                    padding: "7px 13px",
                                                    borderRadius: "20px",
                                                    fontSize: "12px",
                                                }}
                                            >
                                                {getStatus(proposal)}
                                            </span>
                                        </div>

                                        <hr />

                                        {/* PREMIUM SUMMARY */}
                                        <h6 className="fw-semibold" style={{ color: "#123b6d" }}>
                                            Premium Summary
                                        </h6>

                                        <div
                                            className="p-3"
                                            style={{
                                                backgroundColor: "#f8fafc",
                                                borderRadius: "10px",
                                            }}
                                        >
                                            <div className="d-flex justify-content-between mb-3">
                                                <span className="text-muted">Base Premium</span>
                                                <span className="fw-semibold">
                                                    ₹ {proposal.basePremium}
                                                </span>
                                            </div>

                                            <div className="d-flex justify-content-between mb-3">
                                                <span className="text-muted">Discount</span>
                                                <span
                                                    className="fw-semibold"
                                                    style={{ color: "#198754" }}
                                                >
                                                    ₹ {proposal.discount}
                                                </span>
                                            </div>

                                            <hr />

                                            <div className="d-flex justify-content-between">
                                                <span
                                                    className="fw-semibold"
                                                    style={{ color: "#123b6d" }}
                                                >
                                                    Payable Premium
                                                </span>
                                                <span
                                                    className="fw-bold"
                                                    style={{ color: "#1261a0" }}
                                                >
                                                    ₹ {proposal.basePremium - proposal.discount}
                                                </span>
                                            </div>
                                        </div>

                                        {/* PROGRESS BAR */}
                                        <div className="mt-4">
                                            <div className="d-flex justify-content-between mb-2">
                                                <span className="fw-semibold">Application Status</span>
                                                <span
                                                    className="fw-semibold"
                                                    style={{ color: getStatusColor(proposal) }}
                                                >
                                                    {getProgress(proposal)}%
                                                </span>
                                            </div>

                                            <div className="progress" style={{ height: "7px" }}>
                                                <div
                                                    className="progress-bar"
                                                    style={{
                                                        width: `${getProgress(proposal)}%`,
                                                        backgroundColor: getStatusColor(proposal),
                                                    }}
                                                />
                                            </div>

                                            <small className="text-muted">
                                                Current status: {getStatus(proposal)}
                                            </small>
                                        </div>

                                        {/* PAYMENT ACTION REQUIRED */}
                                        {proposal.proposalStatus === "APPROVED" &&
                                            !proposal.paymentCompleted &&
                                            !proposal.paymentPending && (
                                                <div
                                                    className="mt-4 pt-3"
                                                    style={{ borderTop: "1px solid #edf0f4" }}
                                                >
                                                    <div className="d-flex justify-content-between align-items-center">
                                                        <div>
                                                            <div
                                                                className="fw-semibold"
                                                                style={{ color: "#123b6d" }}
                                                            >
                                                                Payment Required
                                                            </div>
                                                            <small className="text-muted">
                                                                Complete your premium payment.
                                                            </small>
                                                        </div>

                                                        <button
                                                            className="btn px-4"
                                                            onClick={() => handlePayment(proposal.id)}
                                                            style={{
                                                                backgroundColor: "#1261a0",
                                                                color: "white",
                                                                borderRadius: "8px",
                                                            }}
                                                        >
                                                            Pay Premium
                                                        </button>
                                                    </div>
                                                </div>
                                            )}

                                        {/* PAYMENT PENDING NOTICE */}
                                        {proposal.proposalStatus === "APPROVED" &&
                                            proposal.paymentPending &&
                                            !proposal.paymentCompleted && (
                                                <div className="mt-4">
                                                    <div
                                                        className="p-3"
                                                        style={{
                                                            backgroundColor: "#fff8e8",
                                                            borderRadius: "9px",
                                                        }}
                                                    >
                                                        <div
                                                            className="fw-semibold"
                                                            style={{ color: "#a66a00" }}
                                                        >
                                                            Payment Pending
                                                        </div>
                                                        <small className="text-muted">
                                                            Your payment is still being processed.
                                                        </small>
                                                    </div>
                                                </div>
                                            )}

                                        {/* PAYMENT SUCCESS NOTICE */}
                                        {proposal.proposalStatus === "APPROVED" &&
                                            proposal.paymentCompleted && (
                                                <div className="mt-4">
                                                    <div
                                                        className="p-3"
                                                        style={{
                                                            backgroundColor: "#eaf7ef",
                                                            borderRadius: "9px",
                                                        }}
                                                    >
                                                        <div
                                                            className="fw-semibold"
                                                            style={{ color: "#198754" }}
                                                        >
                                                            <i className="bi bi-check-circle-fill me-2"></i>
                                                            Payment Completed
                                                        </div>
                                                        <small className="text-muted">
                                                            Your payment was successful.
                                                        </small>
                                                    </div>
                                                </div>
                                            )}

                                        {/* POLICY ISSUED NOTICE */}
                                        {proposal.proposalStatus === "POLICY_ISSUED" && (
                                            <div className="mt-4">
                                                <div
                                                    className="p-3"
                                                    style={{
                                                        backgroundColor: "#eaf7ef",
                                                        borderRadius: "9px",
                                                    }}
                                                >
                                                    <div
                                                        className="fw-semibold"
                                                        style={{ color: "#198754" }}
                                                    >
                                                        <i className="bi bi-shield-check me-2"></i>
                                                        Policy Issued
                                                    </div>
                                                    <small className="text-muted">
                                                        Your insurance policy has been generated successfully.
                                                    </small>
                                                </div>
                                            </div>
                                        )}
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    )
}

export default HolderProposals