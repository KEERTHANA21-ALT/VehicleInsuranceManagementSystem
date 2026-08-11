
import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";

import EmployeeNavbar from "./EmployeeNavbar";
import { getAssignedSurveys } from "../Actions/SurveyActions";

function EmployeeCompletedSurveys() {

    const dispatch = useDispatch();

    const surveys = useSelector(
        state => state.surveySlice.surveys || []
    );


    // =========================================================
    // FETCH SURVEYS
    // =========================================================

    useEffect(() => {

        dispatch(getAssignedSurveys());

    }, [dispatch]);


    // =========================================================
    // COMPLETED SURVEYS
    // =========================================================

    const completedSurveys = surveys.filter(
        survey =>
            survey.claimStatus === "UNDER_REVIEW"
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
                        Completed Surveys
                    </h2>

                    <p className="text-muted mb-0">
                        View vehicle inspections that you have completed.
                    </p>

                </div>


                {/* SUMMARY */}

                <div className="row mb-4">

                    <div className="col-md-4">

                        <div className="card border-0 shadow-sm">

                            <div className="card-body p-4">

                                <p className="text-muted mb-2">
                                    Completed Surveys
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


                {/* TABLE */}

                <div className="card border-0 shadow-sm">

                    <div className="card-body p-4">

                        <div className="d-flex justify-content-between align-items-center mb-4">

                            <div>

                                <h5
                                    className="fw-bold mb-1"
                                    style={{
                                        color: "#123b6d"
                                    }}
                                >
                                    Completed Survey Reports
                                </h5>

                                <small className="text-muted">
                                    Vehicle inspections completed by you.
                                </small>

                            </div>


                            <span
                                className="badge px-3 py-2"
                                style={{
                                    backgroundColor: "#e8f5e9",
                                    color: "#198754"
                                }}
                            >
                                {completedSurveys.length} Completed
                            </span>

                        </div>


                        {completedSurveys.length === 0 ? (

                            <div className="text-center py-5">

                                <i
                                    className="bi bi-clipboard-x"
                                    style={{
                                        fontSize: "40px",
                                        color: "#8b98a7"
                                    }}
                                ></i>

                                <h5 className="fw-bold mt-3">
                                    No Completed Surveys
                                </h5>

                                <p className="text-muted">
                                    You have not completed any vehicle inspections yet.
                                </p>

                            </div>

                        ) : (

                            <div className="table-responsive">

                                <table className="table table-hover align-middle">

                                    <thead>

                                        <tr>

                                            <th>S.No</th>
                                            <th>Claim ID</th>
                                            <th>Vehicle</th>
                                            <th>Claim Amount</th>
                                            <th>Reason</th>
                                            <th>Description</th>
                                            <th>Status</th>

                                        </tr>

                                    </thead>


                                    <tbody>

                                        {completedSurveys.map(
                                            (survey, index) => (

                                                <tr key={survey.id}>

                                                    <td>
                                                        {index + 1}
                                                    </td>


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


                                                    <td>

                                                        <span className="badge bg-light text-dark">

                                                            {survey.vehicleNumber ||
                                                                "N/A"}

                                                        </span>

                                                    </td>


                                                    <td>

                                                        <span className="fw-semibold">

                                                            ₹
                                                            {Number(
                                                                survey.claimAmount || 0
                                                            ).toLocaleString(
                                                                "en-IN"
                                                            )}

                                                        </span>

                                                    </td>


                                                    <td>

                                                        {survey.claimReason ||
                                                            "N/A"}

                                                    </td>


                                                    <td
                                                        style={{
                                                            maxWidth: "250px"
                                                        }}
                                                    >

                                                        <span className="text-muted">

                                                            {survey.claimRemarks ||
                                                                "No description"}

                                                        </span>

                                                    </td>


                                                    <td>

                                                        <span
                                                            className="badge px-3 py-2"
                                                            style={{
                                                                backgroundColor: "#e8f5e9",
                                                                color: "#198754"
                                                            }}
                                                        >
                                                            Inspection Completed
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

export default EmployeeCompletedSurveys;
