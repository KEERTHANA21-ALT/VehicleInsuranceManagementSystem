
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";

import EmployeeNavbar from "./EmployeeNavbar";

import {
    getAssignedSurveys,
    updateSurvey
} from "../Actions/SurveyActions";

function EmployeeAssignedSurveys() {

    const dispatch = useDispatch();

    // =========================================================
    // GET SURVEYS FROM REDUX
    // =========================================================

    const surveys = useSelector(
        state => state.surveySlice.surveys || []
    );

    // =========================================================
    // CLAIM AMOUNT ENTERED BY SURVEYOR
    // =========================================================

    const [claimAmounts, setClaimAmounts] = useState({});

    // =========================================================
    // FETCH ASSIGNED CLAIMS
    // =========================================================

    useEffect(() => {

        dispatch(getAssignedSurveys());

    }, [dispatch]);

    // =========================================================
    // AMOUNT CHANGE
    // =========================================================

    const handleAmountChange = (id, amount) => {

        setClaimAmounts(prev => ({
            ...prev,
            [id]: amount
        }));

    };

    // =========================================================
    // COMPLETE INSPECTION
    // =========================================================

    const handleUpdate = async (survey) => {

        const amount = claimAmounts[survey.id];

        // Validate amount
        if (!amount || Number(amount) <= 0) {

            alert("Please enter the inspected claim amount.");

            return;
        }

        // Data sent to backend
        const surveyData = {

            claimAmount: Number(amount),

            claimReason: survey.claimReason,

            claimRemarks: survey.claimRemarks,

            claimStatus: "UNDER_REVIEW"
        };

        console.log(
            "Survey review data:",
            surveyData
        );

        try {

            await dispatch(
                updateSurvey(
                    survey.id,
                    surveyData
                )
            );

            // Clear entered amount
            setClaimAmounts(prev => {

                const updated = {
                    ...prev
                };

                delete updated[survey.id];

                return updated;

            });

            // Get latest data from backend
            dispatch(getAssignedSurveys());

            alert(
                "Inspection completed successfully. Claim sent to Claim Manager."
            );

        } catch (error) {

            console.log(
                "Survey update error:",
                error.response?.data ||
                error.message
            );

            if (error.response?.status === 401) {

                alert(
                    "Session expired or unauthorized. Please login again."
                );

            } else {

                alert(
                    "Failed to update inspection."
                );

            }

        }

    };

    // =========================================================
    // COUNTS
    // =========================================================

    const pendingSurveys = surveys.filter(
        survey =>
            survey.claimStatus === "SUBMITTED"
    );

    const completedSurveys = surveys.filter(
        survey =>
            survey.claimStatus === "UNDER_REVIEW"
    );

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

                {/* HEADER */}

                <div className="mb-4">

                    <h2
                        className="fw-bold mb-1"
                        style={{
                            color: "#123b6d"
                        }}
                    >
                        Assigned Surveys
                    </h2>

                    <p className="text-muted mb-0">
                        Inspect the assigned vehicles and submit your inspection report.
                    </p>

                </div>


                {/* SUMMARY CARDS */}

                <div className="row g-4 mb-4">

                    {/* ASSIGNED */}

                    <div className="col-md-4">

                        <div
                            className="card border-0 shadow-sm h-100"
                            style={{
                                borderRadius: "14px"
                            }}
                        >

                            <div className="card-body p-4">

                                <p className="text-muted mb-2">
                                    Assigned Claims
                                </p>

                                <h2
                                    className="fw-bold mb-0"
                                    style={{
                                        color: "#1261a0"
                                    }}
                                >
                                    {surveys.length}
                                </h2>

                            </div>

                        </div>

                    </div>


                    {/* PENDING */}

                    <div className="col-md-4">

                        <div
                            className="card border-0 shadow-sm h-100"
                            style={{
                                borderRadius: "14px"
                            }}
                        >

                            <div className="card-body p-4">

                                <p className="text-muted mb-2">
                                    Pending Inspections
                                </p>

                                <h2
                                    className="fw-bold mb-0"
                                    style={{
                                        color: "#d97706"
                                    }}
                                >
                                    {pendingSurveys.length}
                                </h2>

                            </div>

                        </div>

                    </div>


                    {/* COMPLETED */}

                    <div className="col-md-4">

                        <div
                            className="card border-0 shadow-sm h-100"
                            style={{
                                borderRadius: "14px"
                            }}
                        >

                            <div className="card-body p-4">

                                <p className="text-muted mb-2">
                                    Sent to Claim Manager
                                </p>

                                <h2
                                    className="fw-bold mb-0"
                                    style={{
                                        color: "#198754"
                                    }}
                                >
                                    {completedSurveys.length}
                                </h2>

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

                        {/* TABLE HEADER */}

                        <div className="d-flex justify-content-between align-items-center mb-4">

                            <div>

                                <h5
                                    className="fw-bold mb-1"
                                    style={{
                                        color: "#123b6d"
                                    }}
                                >
                                    My Assigned Claims
                                </h5>

                                <small className="text-muted">
                                    Inspect the vehicle and submit the inspection result.
                                </small>

                            </div>

                            <span
                                className="badge px-3 py-2"
                                style={{
                                    backgroundColor: "#e8f1f8",
                                    color: "#1261a0",
                                    fontSize: "13px"
                                }}
                            >
                                {surveys.length} Claims
                            </span>

                        </div>


                        {/* EMPTY STATE */}

                        {surveys.length === 0 ? (

                            <div className="text-center py-5">

                                <div
                                    className="d-flex align-items-center justify-content-center mx-auto"
                                    style={{
                                        width: "80px",
                                        height: "80px",
                                        borderRadius: "50%",
                                        backgroundColor: "#eef2f6"
                                    }}
                                >

                                    <i
                                        className="bi bi-clipboard-x"
                                        style={{
                                            fontSize: "35px",
                                            color: "#8b98a7"
                                        }}
                                    ></i>

                                </div>

                                <h5 className="fw-bold mt-4 mb-2">
                                    No Assigned Claims
                                </h5>

                                <p className="text-muted mb-0">
                                    You currently have no claims assigned to you.
                                </p>

                            </div>

                        ) : (

                            /* TABLE */

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
                                                Claim Amount
                                            </th>

                                            <th className="py-3">
                                                Reason
                                            </th>

                                            <th className="py-3">
                                                Description
                                            </th>

                                            <th className="py-3">
                                                Status
                                            </th>

                                            <th className="py-3">
                                                Action
                                            </th>

                                        </tr>

                                    </thead>


                                    <tbody>

                                        {surveys.map(
                                            (survey, index) => (

                                                <tr key={survey.id}>

                                                    {/* S.NO */}

                                                    <td className="fw-semibold text-muted">
                                                        {index + 1}
                                                    </td>


                                                    {/* CLAIM ID */}

                                                    <td>

                                                        <span
                                                            className="fw-bold"
                                                            style={{
                                                                color: "#1261a0"
                                                            }}
                                                        >
                                                            #{survey.id}
                                                        </span>

                                                    </td>


                                                    {/* VEHICLE */}

                                                    <td>

                                                        <span
                                                            className="badge"
                                                            style={{
                                                                backgroundColor: "#f1f3f5",
                                                                color: "#495057",
                                                                fontWeight: "500"
                                                            }}
                                                        >
                                                            {survey.vehicleNumber || "N/A"}
                                                        </span>

                                                    </td>


                                                    {/* CLAIM AMOUNT */}

                                                    <td>

                                                        {survey.claimStatus === "SUBMITTED" ? (

                                                            <input
                                                                type="number"
                                                                className="form-control form-control-sm"
                                                                placeholder="Enter amount"
                                                                min="1"
                                                                value={
                                                                    claimAmounts[survey.id] || ""
                                                                }
                                                                onChange={(e) =>
                                                                    handleAmountChange(
                                                                        survey.id,
                                                                        e.target.value
                                                                    )
                                                                }
                                                                style={{
                                                                    width: "150px"
                                                                }}
                                                            />

                                                        ) : (

                                                            <span className="fw-semibold">

                                                                ₹
                                                                {survey.claimAmount
                                                                    ? Number(
                                                                        survey.claimAmount
                                                                    ).toLocaleString("en-IN")
                                                                    : "0"
                                                                }

                                                            </span>

                                                        )}

                                                    </td>


                                                    {/* REASON */}

                                                    <td>

                                                        <span className="fw-semibold">

                                                            {survey.claimReason || "N/A"}

                                                        </span>

                                                    </td>


                                                    {/* DESCRIPTION */}

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

                                                            {survey.claimRemarks ||
                                                                "No description"}

                                                        </span>

                                                    </td>


                                                    {/* STATUS */}

                                                    <td>

                                                        {survey.claimStatus === "SUBMITTED" ? (

                                                            <span
                                                                className="badge px-3 py-2"
                                                                style={{
                                                                    backgroundColor: "#fff4df",
                                                                    color: "#d97706"
                                                                }}
                                                            >
                                                                Pending Inspection
                                                            </span>

                                                        ) : survey.claimStatus === "UNDER_REVIEW" ? (

                                                            <span
                                                                className="badge px-3 py-2"
                                                                style={{
                                                                    backgroundColor: "#e8f5e9",
                                                                    color: "#198754"
                                                                }}
                                                            >
                                                                Inspection Completed
                                                            </span>

                                                        ) : (

                                                            <span
                                                                className="badge px-3 py-2"
                                                                style={{
                                                                    backgroundColor: "#f1f3f5",
                                                                    color: "#495057"
                                                                }}
                                                            >
                                                                {survey.claimStatus}
                                                            </span>

                                                        )}

                                                    </td>


                                                    {/* ACTION */}

                                                    <td>

                                                        {survey.claimStatus === "SUBMITTED" ? (

                                                            <button
                                                                className="btn btn-sm px-3"
                                                                style={{
                                                                    backgroundColor: "#1261a0",
                                                                    color: "white",
                                                                    border: "none",
                                                                    borderRadius: "6px"
                                                                }}
                                                                onClick={() =>
                                                                    handleUpdate(survey)
                                                                }
                                                            >
                                                                Complete Inspection
                                                            </button>

                                                        ) : survey.claimStatus === "UNDER_REVIEW" ? (

                                                            <span
                                                                className="text-success"
                                                                style={{
                                                                    fontSize: "14px"
                                                                }}
                                                            >

                                                                <i className="bi bi-check-circle me-1"></i>

                                                                Sent to Claim Manager

                                                            </span>

                                                        ) : (

                                                            <span className="text-muted">
                                                                -
                                                            </span>

                                                        )}

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

export default EmployeeAssignedSurveys;

