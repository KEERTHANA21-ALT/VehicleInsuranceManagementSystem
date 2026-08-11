
import { useEffect, useState } from "react";
import axios from "axios";
import EmployeeNavbar from "./EmployeeNavbar";

function EmployeeClaimPayments() {

    const [claims, setClaims] = useState([]);
    const [loading, setLoading] = useState(true);
    const [payingId, setPayingId] = useState(null);


    useEffect(() => {

        const fetchClaims = async () => {

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

        fetchClaims();

    }, []);


    const handlePayment = async (id) => {

        const confirmPayment = window.confirm(
            "Are you sure you want to process this claim payment?"
        );

        if (!confirmPayment) {
            return;
        }

        try {

            setPayingId(id);

            const token = localStorage.getItem("token");

            await axios.put(
                `http://localhost:8080/api/claim/update/${id}`,
                {
                    claimStatus: "PAID"
                },
                {
                    headers: {
                        Authorization: "Bearer " + token
                    }
                }
            );


            // Change status to PAID
            setClaims(prevClaims =>
                prevClaims.map(claim =>
                    claim.id === id
                        ? {
                            ...claim,
                            claimStatus: "PAID"
                        }
                        : claim
                )
            );

            alert("Claim payment completed successfully.");

        } catch (error) {

            console.log(
                "Payment error:",
                error.response?.data || error.message
            );

            alert("Failed to process claim payment.");

        } finally {

            setPayingId(null);

        }

    };


    // Only APPROVED claims
    const pendingClaims = claims.filter(
        claim => claim.claimStatus === "APPROVED"
    );


    // Only PAID claims
    const paidClaims = claims.filter(
        claim => claim.claimStatus === "PAID"
    );


    return (

        <div
            className="min-vh-100"
            style={{
                backgroundColor: "#f5f7fa"
            }}
        >

            <EmployeeNavbar />


            <div className="container py-5">


                {/* HEADER */}

                <div className="mb-4">

                    <h2
                        className="fw-bold mb-1"
                        style={{
                            color: "#123b6d"
                        }}
                    >
                        Claim Payments
                    </h2>

                    <p className="text-muted mb-0">
                        Process payments for approved customer claims.
                    </p>

                </div>


                {/* SUMMARY */}

                <div className="row mb-4">

                    {/* PENDING */}

                    <div className="col-md-4">

                        <div
                            className="card border-0 shadow-sm"
                            style={{
                                borderRadius: "14px"
                            }}
                        >

                            <div className="card-body p-4">

                                <div className="d-flex justify-content-between align-items-center">

                                    <div>

                                        <p className="text-muted mb-2">
                                            Payments Pending
                                        </p>

                                        <h2
                                            className="fw-bold mb-0"
                                            style={{
                                                color: "#1261a0"
                                            }}
                                        >
                                            {pendingClaims.length}
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


                    {/* PAID */}

                    <div className="col-md-4">

                        <div
                            className="card border-0 shadow-sm"
                            style={{
                                borderRadius: "14px"
                            }}
                        >

                            <div className="card-body p-4">

                                <div className="d-flex justify-content-between align-items-center">

                                    <div>

                                        <p className="text-muted mb-2">
                                            Payments Completed
                                        </p>

                                        <h2
                                            className="fw-bold mb-0"
                                            style={{
                                                color: "#198754"
                                            }}
                                        >
                                            {paidClaims.length}
                                        </h2>

                                    </div>

                                    <div
                                        className="d-flex align-items-center justify-content-center"
                                        style={{
                                            width: "52px",
                                            height: "52px",
                                            borderRadius: "12px",
                                            backgroundColor: "#e8f5e9"
                                        }}
                                    >

                                        <i
                                            className="bi bi-check-circle"
                                            style={{
                                                fontSize: "24px",
                                                color: "#198754"
                                            }}
                                        ></i>

                                    </div>

                                </div>

                            </div>

                        </div>

                    </div>

                </div>


                {/* CLAIMS CARD */}

                <div
                    className="card border-0 shadow-sm"
                    style={{
                        borderRadius: "14px"
                    }}
                >

                    <div className="card-body p-4">


                        {/* HEADER */}

                        <div className="d-flex justify-content-between align-items-center mb-4">

                            <div>

                                <h5
                                    className="fw-bold mb-1"
                                    style={{
                                        color: "#123b6d"
                                    }}
                                >
                                    Claim Payments
                                </h5>

                                <small className="text-muted">
                                    Approved and completed claim payments.
                                </small>

                            </div>

                            <div>

                                <span
                                    className="badge px-3 py-2 me-2"
                                    style={{
                                        backgroundColor: "#fff3cd",
                                        color: "#856404"
                                    }}
                                >
                                    {pendingClaims.length} Pending
                                </span>

                                <span
                                    className="badge px-3 py-2"
                                    style={{
                                        backgroundColor: "#d1e7dd",
                                        color: "#198754"
                                    }}
                                >
                                    {paidClaims.length} Paid
                                </span>

                            </div>

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

                                <div
                                    className="d-flex align-items-center justify-content-center mx-auto"
                                    style={{
                                        width: "80px",
                                        height: "80px",
                                        borderRadius: "50%",
                                        backgroundColor: "#e8f5e9"
                                    }}
                                >

                                    <i
                                        className="bi bi-check-circle"
                                        style={{
                                            fontSize: "35px",
                                            color: "#198754"
                                        }}
                                    ></i>

                                </div>

                                <h5 className="fw-bold mt-4 mb-2">
                                    No Claims
                                </h5>

                                <p className="text-muted mb-0">
                                    There are no claims available.
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
                                                Claim ID
                                            </th>

                                            <th className="py-3">
                                                Vehicle
                                            </th>

                                            <th className="py-3">
                                                Approved Amount
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

                                        {/* PENDING CLAIMS */}

                                        {pendingClaims.map(
                                            (claim, index) => (

                                                <tr key={claim.id}>

                                                    <td className="fw-semibold text-muted">
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
                                                                color: "#495057",
                                                                fontWeight: "500"
                                                            }}
                                                        >
                                                            {claim.vehicleNumber || "N/A"}
                                                        </span>

                                                    </td>

                                                    <td>

                                                        <span
                                                            className="fw-bold"
                                                            style={{
                                                                color: "#198754"
                                                            }}
                                                        >
                                                            ₹
                                                            {Number(
                                                                claim.claimAmount || 0
                                                            ).toLocaleString("en-IN")}
                                                        </span>

                                                    </td>

                                                    <td>

                                                        <span
                                                            className="badge px-3 py-2"
                                                            style={{
                                                                backgroundColor: "#fff3cd",
                                                                color: "#856404"
                                                            }}
                                                        >
                                                            Approved
                                                        </span>

                                                    </td>

                                                    <td className="text-center">

                                                        <button
                                                            className="btn btn-sm btn-success px-3"
                                                            disabled={
                                                                payingId === claim.id
                                                            }
                                                            onClick={() =>
                                                                handlePayment(
                                                                    claim.id
                                                                )
                                                            }
                                                        >

                                                            {payingId === claim.id ? (

                                                                <>
                                                                    <span className="spinner-border spinner-border-sm me-2"></span>
                                                                    Processing...
                                                                </>

                                                            ) : (

                                                                <>
                                                                    <i className="bi bi-credit-card me-1"></i>
                                                                    Pay Claim
                                                                </>

                                                            )}

                                                        </button>

                                                    </td>

                                                </tr>

                                            )
                                        )}


                                        {/* PAID CLAIMS */}

                                        {paidClaims.map(
                                            (claim, index) => (

                                                <tr key={claim.id}>

                                                    <td className="fw-semibold text-muted">
                                                        {pendingClaims.length + index + 1}
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
                                                                color: "#495057",
                                                                fontWeight: "500"
                                                            }}
                                                        >
                                                            {claim.vehicleNumber || "N/A"}
                                                        </span>

                                                    </td>

                                                    <td>

                                                        <span
                                                            className="fw-bold"
                                                            style={{
                                                                color: "#198754"
                                                            }}
                                                        >
                                                            ₹
                                                            {Number(
                                                                claim.claimAmount || 0
                                                            ).toLocaleString("en-IN")}
                                                        </span>

                                                    </td>

                                                    <td>

                                                        <span
                                                            className="badge px-3 py-2"
                                                            style={{
                                                                backgroundColor: "#d1e7dd",
                                                                color: "#198754"
                                                            }}
                                                        >
                                                            Paid
                                                        </span>

                                                    </td>

                                                    <td className="text-center">

                                                        <span
                                                            className="badge px-3 py-2"
                                                            style={{
                                                                backgroundColor: "#d1e7dd",
                                                                color: "#198754"
                                                            }}
                                                        >
                                                            ✓ Payment Completed
                                                        </span>

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

export default EmployeeClaimPayments;

