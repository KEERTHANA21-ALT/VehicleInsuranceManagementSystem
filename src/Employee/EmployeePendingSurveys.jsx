import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router";

import EmployeeNavbar from "./EmployeeNavbar";
import { getAssignedSurveys } from "../Actions/SurveyActions";

function EmployeePendingSurveys() {

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const surveys = useSelector(
        state => state.surveySlice.surveys || []
    );

    // Only claims waiting for surveyor inspection
    const pendingSurveys = surveys.filter(
        survey => survey.claimStatus === "SUBMITTED"
    );


    useEffect(() => {

        dispatch(getAssignedSurveys());

    }, [dispatch]);


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
                        Pending Surveys
                    </h2>

                    <p className="text-muted mb-0">
                        Complete vehicle inspections assigned to you.
                    </p>

                </div>


                {/* SUMMARY */}

                <div className="row mb-4">

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
                                            Pending Surveys
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


                                    <div
                                        className="d-flex align-items-center justify-content-center"
                                        style={{
                                            width: "50px",
                                            height: "50px",
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

                </div>


                {/* TABLE */}

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
                                    Pending Inspections
                                </h5>

                                <small className="text-muted">
                                    Claims waiting for your vehicle inspection.
                                </small>

                            </div>


                            <span
                                className="badge px-3 py-2"
                                style={{
                                    backgroundColor: "#fff4df",
                                    color: "#d97706",
                                    fontSize: "13px"
                                }}
                            >
                                {pendingSurveys.length} Pending
                            </span>

                        </div>


                        {/* EMPTY STATE */}

                        {pendingSurveys.length === 0 ? (

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
                                        className="bi bi-check-circle"
                                        style={{
                                            fontSize: "35px",
                                            color: "#198754"
                                        }}
                                    ></i>

                                </div>


                                <h5
                                    className="fw-bold mt-4 mb-2"
                                >
                                    No Pending Surveys
                                </h5>


                                <p className="text-muted mb-0">
                                    You currently have no vehicle inspections pending.
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
                                                Customer
                                            </th>

                                            <th className="py-3">
                                                Vehicle
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

                                        {pendingSurveys.map(
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


                                                    {/* CUSTOMER */}

                                                    <td>

                                                        <span className="fw-semibold">
                                                            {
                                                                survey.policyHolderName ||
                                                                "Customer"
                                                            }
                                                        </span>

                                                    </td>


                                                    {/* VEHICLE */}

                                                    <td>

                                                        <span
                                                            className="badge"
                                                            style={{
                                                                backgroundColor:
                                                                    "#f1f3f5",
                                                                color:
                                                                    "#495057"
                                                            }}
                                                        >
                                                            {
                                                                survey.vehicleNumber ||
                                                                "N/A"
                                                            }
                                                        </span>

                                                    </td>


                                                    {/* REASON */}

                                                    <td>

                                                        <span className="fw-semibold">
                                                            {survey.claimReason}
                                                        </span>

                                                    </td>


                                                    {/* DESCRIPTION */}

                                                    <td
                                                        style={{
                                                            maxWidth: "230px"
                                                        }}
                                                    >

                                                        <span className="text-muted">
                                                            {
                                                                survey.claimRemarks ||
                                                                "No description"
                                                            }
                                                        </span>

                                                    </td>


                                                    {/* STATUS */}

                                                    <td>

                                                        <span
                                                            className="badge px-3 py-2"
                                                            style={{
                                                                backgroundColor:
                                                                    "#fff4df",
                                                                color:
                                                                    "#d97706"
                                                            }}
                                                        >
                                                            Pending Inspection
                                                        </span>

                                                    </td>


                                                    {/* ACTION */}

                                                    <td>

                                                        <button
                                                            className="btn btn-sm px-3"
                                                            style={{
                                                                backgroundColor:
                                                                    "#1261a0",
                                                                color: "white",
                                                                border: "none",
                                                                borderRadius: "6px"
                                                            }}
                                                            onClick={() =>
                                                                navigate(
                                                                    `/employee/survey/${survey.id}`
                                                                )
                                                            }
                                                        >
                                                            Start Inspection
                                                        </button>

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

export default EmployeePendingSurveys