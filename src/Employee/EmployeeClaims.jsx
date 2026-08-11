
import { useEffect, useState } from "react";
import axios from "axios";
import EmployeeNavbar from "./EmployeeNavbar";

function EmployeeClaims() {

    const [claims, setClaims] = useState([]);
    const [approvedClaims, setApprovedClaims] = useState([]);

    const [loading, setLoading] = useState(true);
    const [paymentLoading, setPaymentLoading] = useState(true);


    // =========================================================
    // FETCH CLAIMS UNDER REVIEW
    // =========================================================

    useEffect(() => {

        fetchClaims();
        fetchApprovedClaims();

    }, []);


    const fetchClaims = async () => {

        try {

            const token = localStorage.getItem("token");

            const response = await axios.get(
                "http://localhost:8080/api/claim/manager/pending",
                {
                    headers: {
                        Authorization: "Bearer " + token
                    }
                }
            );

            setClaims(response.data || []);

        } catch (error) {

            console.log(
                "Error fetching claims:",
                error.response?.data || error.message
            );

        } finally {

            setLoading(false);

        }

    };


    // =========================================================
    // FETCH APPROVED CLAIMS WAITING FOR PAYMENT
    // =========================================================

    const fetchApprovedClaims = async () => {

        try {

            const token = localStorage.getItem("token");

            const response = await axios.get(
                "http://localhost:8080/api/claim/insurance-manager/payment",
                {
                    headers: {
                        Authorization: "Bearer " + token
                    }
                }
            );

            setApprovedClaims(response.data || []);

        } catch (error) {

            console.log(
                "Error fetching approved claims:",
                error.response?.data || error.message
            );

        } finally {

            setPaymentLoading(false);

        }

    };


    // =========================================================
    // APPROVE / REJECT
    // =========================================================

    const handleDecision = async (id, status) => {

        try {

            const token = localStorage.getItem("token");

            await axios.put(
                `http://localhost:8080/api/claim/manager/decision/${id}`,
                {
                    claimStatus: status
                },
                {
                    headers: {
                        Authorization: "Bearer " + token
                    }
                }
            );


            // Remove from UNDER_REVIEW list

            setClaims(prev =>
                prev.filter(claim => claim.id !== id)
            );


            // If approved, refresh payment list

            if (status === "APPROVED") {
                fetchApprovedClaims();
            }


            alert(
                status === "APPROVED"
                    ? "Claim approved successfully."
                    : "Claim rejected successfully."
            );

        } catch (error) {

            console.log(
                "Decision error:",
                error.response?.data || error.message
            );

            alert("Failed to update claim.");

        }

    };


    // =========================================================
    // PAY CLAIM
    // =========================================================

    const handlePayment = async (id) => {

        const confirmPayment = window.confirm(
            "Are you sure you want to pay this claim?"
        );

        if (!confirmPayment) {
            return;
        }


        try {

            const token = localStorage.getItem("token");

            await axios.put(
                `http://localhost:8080/api/claim/pay/${id}`,
                {},
                {
                    headers: {
                        Authorization: "Bearer " + token
                    }
                }
            );


            // Remove from payment pending list

            setApprovedClaims(prev =>
                prev.filter(claim => claim.id !== id)
            );


            alert(
                "Claim payment completed successfully."
            );

        } catch (error) {

            console.log(
                "Payment error:",
                error.response?.data || error.message
            );

            alert(
                "Failed to process claim payment."
            );

        }

    };


    // =========================================================
    // TOTAL COUNTS
    // =========================================================

    const underReviewCount = claims.length;

    const paymentPendingCount =
        approvedClaims.length;


    // =========================================================
    // UI
    // =========================================================

    return (

        <div
            className="min-vh-100"
            style={{
                backgroundColor: "#f5f7fa"
            }}
        >

            <EmployeeNavbar />


            <div className="container py-5">


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
                        Claims Management
                    </h2>

                    <p className="text-muted mb-0">
                        Review inspected claims, make decisions and process approved payments.
                    </p>

                </div>


                {/* =================================================
                    SUMMARY CARDS
                ================================================= */}

                <div className="row g-4 mb-5">


                    {/* UNDER REVIEW */}

                    <div className="col-md-4">

                        <div
                            className="card border-0 shadow-sm h-100"
                            style={{
                                borderRadius: "14px"
                            }}
                        >

                            <div className="card-body p-4">

                                <div className="d-flex justify-content-between align-items-center">

                                    <div>

                                        <p className="text-muted mb-2">
                                            Under Review
                                        </p>

                                        <h2
                                            className="fw-bold mb-0"
                                            style={{
                                                color: "#d97706"
                                            }}
                                        >
                                            {underReviewCount}
                                        </h2>

                                    </div>

                                    <div
                                        className="d-flex align-items-center justify-content-center"
                                        style={{
                                            width: "52px",
                                            height: "52px",
                                            borderRadius: "12px",
                                            backgroundColor: "#fff4df"
                                        }}
                                    >

                                        <i
                                            className="bi bi-hourglass-split"
                                            style={{
                                                fontSize: "24px",
                                                color: "#d97706"
                                            }}
                                        ></i>

                                    </div>

                                </div>

                            </div>

                        </div>

                    </div>


                    {/* APPROVED */}

                    <div className="col-md-4">

                        <div
                            className="card border-0 shadow-sm h-100"
                            style={{
                                borderRadius: "14px"
                            }}
                        >

                            <div className="card-body p-4">

                                <div className="d-flex justify-content-between align-items-center">

                                    <div>

                                        <p className="text-muted mb-2">
                                            Payment Pending
                                        </p>

                                        <h2
                                            className="fw-bold mb-0"
                                            style={{
                                                color: "#1261a0"
                                            }}
                                        >
                                            {paymentPendingCount}
                                        </h2>

                                    </div>

                                    <div
                                        className="d-flex align-items-center justify-content-center"
                                        style={{
                                            width: "52px",
                                            height: "52px",
                                            borderRadius: "12px",
                                            backgroundColor: "#e8f1f8"
                                        }}
                                    >

                                        <i
                                            className="bi bi-credit-card"
                                            style={{
                                                fontSize: "24px",
                                                color: "#1261a0"
                                            }}
                                        ></i>

                                    </div>

                                </div>

                            </div>

                        </div>

                    </div>


                    

                </div>


                {/* =================================================
                    CLAIMS UNDER REVIEW
                ================================================= */}

                <div
                    className="card border-0 shadow-sm mb-5"
                    style={{
                        borderRadius: "14px"
                    }}
                >

                    <div className="card-body p-4">


                        <div className="d-flex justify-content-between align-items-center mb-4">

                            <div>

                                <h5
                                    className="fw-bold mb-1"
                                    style={{
                                        color: "#123b6d"
                                    }}
                                >
                                    Claims Under Review
                                </h5>

                                <small className="text-muted">
                                    Review vehicle inspection reports submitted by surveyors.
                                </small>

                            </div>


                            <span
                                className="badge px-3 py-2"
                                style={{
                                    backgroundColor: "#fff4df",
                                    color: "#d97706"
                                }}
                            >
                                {claims.length} Pending
                            </span>

                        </div>


                        {loading ? (

                            <div className="text-center py-5">

                                <div
                                    className="spinner-border"
                                    style={{
                                        color: "#1261a0"
                                    }}
                                ></div>

                                <p className="text-muted mt-3 mb-0">
                                    Loading claims...
                                </p>

                            </div>

                        ) : claims.length === 0 ? (

                            <div className="text-center py-5">

                                <i
                                    className="bi bi-check-circle"
                                    style={{
                                        fontSize: "45px",
                                        color: "#198754"
                                    }}
                                ></i>

                                <h5 className="fw-bold mt-3">
                                    No Claims Under Review
                                </h5>

                                <p className="text-muted mb-0">
                                    All inspected claims have been processed.
                                </p>

                            </div>

                        ) : (

                            <div className="table-responsive">

                                <table className="table table-hover align-middle mb-0">

                                    <thead>

                                        <tr
                                            style={{
                                                backgroundColor: "#f1f5f9"
                                            }}
                                        >

                                            <th className="py-3">
                                                S.No
                                            </th>

                                            <th className="py-3">
                                                Claim
                                            </th>

                                            <th className="py-3">
                                                Vehicle
                                            </th>

                                            <th className="py-3">
                                                Amount
                                            </th>

                                            <th className="py-3">
                                                Reason
                                            </th>

                                            <th className="py-3">
                                                Remarks
                                            </th>

                                            <th className="py-3">
                                                Status
                                            </th>

                                            <th className="py-3 text-center">
                                                Action
                                            </th>

                                        </tr>

                                    </thead>


                                    <tbody>

                                        {claims.map(
                                            (claim, index) => (

                                                <tr key={claim.id}>

                                                    <td className="text-muted fw-semibold">
                                                        {index + 1}
                                                    </td>


                                                    <td>

                                                        <span
                                                            className="fw-bold"
                                                            style={{
                                                                color: "#1261a0"
                                                            }}
                                                        >
                                                            #{claim.id}
                                                        </span>

                                                    </td>


                                                    <td>

                                                        <span
                                                            className="badge"
                                                            style={{
                                                                backgroundColor: "#f1f3f5",
                                                                color: "#495057"
                                                            }}
                                                        >
                                                            {claim.vehicleNumber || "N/A"}
                                                        </span>

                                                    </td>


                                                    <td>

                                                        <span className="fw-bold">

                                                            ₹
                                                            {Number(
                                                                claim.claimAmount || 0
                                                            ).toLocaleString(
                                                                "en-IN"
                                                            )}

                                                        </span>

                                                    </td>


                                                    <td>
                                                        {claim.claimReason || "N/A"}
                                                    </td>


                                                    <td
                                                        style={{
                                                            maxWidth: "250px"
                                                        }}
                                                    >

                                                        <span
                                                            className="text-muted"
                                                            style={{
                                                                fontSize: "14px"
                                                            }}
                                                        >
                                                            {claim.claimRemarks || "No remarks"}
                                                        </span>

                                                    </td>


                                                    <td>

                                                        <span
                                                            className="badge px-3 py-2"
                                                            style={{
                                                                backgroundColor: "#fff4df",
                                                                color: "#d97706"
                                                            }}
                                                        >
                                                            Under Review
                                                        </span>

                                                    </td>


                                                    <td>

                                                        <div className="d-flex gap-2 justify-content-center">

                                                            <button
                                                                className="btn btn-sm btn-success"
                                                                onClick={() =>
                                                                    handleDecision(
                                                                        claim.id,
                                                                        "APPROVED"
                                                                    )
                                                                }
                                                            >
                                                                
                                                                Approve
                                                            </button>


                                                            <button
                                                                className="btn btn-sm btn-danger"
                                                                onClick={() =>
                                                                    handleDecision(
                                                                        claim.id,
                                                                        "REJECTED"
                                                                    )
                                                                }
                                                            >
                                                                
                                                                Reject
                                                            </button>

                                                        </div>

                                                    </td>

                                                </tr>

                                            )
                                        )}

                                    </tbody>

                                </table>

                            </div>

                        )}

                    </div>

                </div>




            </div>

        </div>

    );

}

export default EmployeeClaims;

