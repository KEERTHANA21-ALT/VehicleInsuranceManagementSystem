import { useState, useEffect } from "react";
import axios from "axios";
import AdminNavbar from "./AdminNavbar";

function InsurancePlanManagement() {
    const [plans, setPlans] = useState([]);

    const [planType, setPlanType] = useState("");
    const [basePremium, setBasePremium] = useState("");
    const [coverageAmount, setCoverageAmount] = useState("");
    const [inspectionRequired, setInspectionRequired] = useState("");

    const [editMode, setEditMode] = useState(false);
    const [selectedId, setSelectedId] = useState(null);

    const token = localStorage.getItem("token");

    const config = {
        headers: {
            Authorization: "Bearer " + token
        }
    };

    // Get All Plans
    const fetchPlans = async () => {
        try {
            const response = await axios.get(
                "http://localhost:8080/api/insurancePlan/get-all",
                config
            );
            console.log(response.data);
            setPlans(response.data);
        } catch (error) {
            console.log(error);
        }
    };

    useEffect(() => {
        fetchPlans();
    }, []);

    // Create / Update Plan
    const savePlan = async () => {
        if (
            planType === "" ||
            basePremium === "" ||
            coverageAmount === "" ||
            inspectionRequired === ""
        ) {
            alert("Fill all fields");
            return;
        }

        const data = {
            planType: planType,
            basePremium: Number(basePremium),
            coverageAmount: Number(coverageAmount),
            inspectionRequired: inspectionRequired === "true"
        };

        try {
            if (editMode) {
                console.log("Updating ID:", selectedId);
                await axios.put(
                    `http://localhost:8080/api/insurancePlan/update/${selectedId}`,
                    data,
                    config
                );
                alert("Insurance Plan Updated Successfully");
            } else {
                await axios.post(
                    "http://localhost:8080/api/insurancePlan/add",
                    data,
                    config
                );
                alert("Insurance Plan Created Successfully");
            }

            clearForm();
            fetchPlans();
        } catch (error) {
            console.log(error);
            alert(
                error.response?.data || "Operation Failed"
            );
        }
    };

    // Load Plan Data For Update
    const editPlan = (plan) => {
        console.log("Selected Plan:", plan);

        setEditMode(true);
        setSelectedId(plan.id);

        setPlanType(plan.planType);
        setBasePremium(plan.basePremium);
        setCoverageAmount(plan.coverageAmount);
        setInspectionRequired(plan.inspectionRequired.toString());

        window.scrollTo({
            top: 0,
            behavior: "smooth"
        });
    };

    // Delete Plan
    const togglePlanStatus = async (id) => {
        try {
            console.log("Sending Plan ID:", id);
            const response = await axios.put(
                `http://localhost:8080/api/insurancePlan/toggle/${id}`,
                {},
                config
            );
            console.log("Backend Response:", response.data);
            fetchPlans();
        } catch (error) {
            console.log(error);
            alert("Status update failed");
        }
    };

    const clearForm = () => {
        setPlanType("");
        setBasePremium("");
        setCoverageAmount("");
        setInspectionRequired("");
        setEditMode(false);
        setSelectedId(null);
    };

    return (
        <div>
            <AdminNavbar />

            <div className="container py-4">
                <h2 className="mb-4">Insurance Plan Management</h2>

                {/* Create / Update Plan */}
                <div className="card shadow mb-4">
                    <div className="card-body">
                        <h4 className="mb-3">
                            {editMode ? "Update Insurance Plan" : "Create Insurance Plan"}
                        </h4>

                        <div className="row">
                            <div className="col-md-6 mb-3">
                                <label>Plan Type</label>
                                <select
                                    className="form-select"
                                    value={planType}
                                    onChange={(e) => setPlanType(e.target.value)}
                                >
                                    <option value="">Select Plan Type</option>
                                    <option value="THIRD_PARTY">Third Party</option>
                                    <option value="OWN_DAMAGE">Own Damage</option>
                                    <option value="COMPREHENSIVE">Comprehensive</option>
                                </select>
                            </div>

                            <div className="col-md-6 mb-3">
                                <label>Base Premium</label>
                                <input
                                    type="number"
                                    className="form-control"
                                    value={basePremium}
                                    onChange={(e) => setBasePremium(e.target.value)}
                                />
                            </div>

                            <div className="col-md-6 mb-3">
                                <label>Coverage Amount</label>
                                <input
                                    type="number"
                                    className="form-control"
                                    value={coverageAmount}
                                    onChange={(e) => setCoverageAmount(e.target.value)}
                                />
                            </div>

                            <div className="col-md-6 mb-3">
                                <label>Inspection Required</label>
                                <select
                                    className="form-select"
                                    value={inspectionRequired}
                                    onChange={(e) => setInspectionRequired(e.target.value)}
                                >
                                    <option value="">Select Option</option>
                                    <option value="true">Yes</option>
                                    <option value="false">No</option>
                                </select>
                            </div>
                        </div>

                        <button
                            className={editMode ? "btn btn-primary me-2" : "btn btn-success me-2"}
                            onClick={savePlan}
                        >
                            {editMode ? "Update Plan" : "Create Plan"}
                        </button>

                        {editMode && (
                            <button className="btn btn-secondary" onClick={clearForm}>
                                Cancel
                            </button>
                        )}
                    </div>
                </div>

                {/* Plan List */}
                <div className="card shadow">
                    <div className="card-body">
                        <h4 className="mb-3">Insurance Plans</h4>

                        <table className="table table-bordered table-hover">
                            <thead className="table-primary">
                                <tr>
                                    <th>S.No</th>
                                    <th>Plan Type</th>
                                    <th>Base Premium</th>
                                    <th>Coverage Amount</th>
                                    <th>Inspection Required</th>
                                    <th>Status</th>
                                    <th>Action</th>
                                </tr>
                            </thead>

                            <tbody>
                                {plans.map((plan, index) => (
                                    <tr key={plan.id}>
                                        <td>{index + 1}</td>
                                        <td>{plan.planType}</td>
                                        <td>₹ {plan.basePremium}</td>
                                        <td>₹ {plan.coverageAmount}</td>
                                        <td>{plan.inspectionRequired ? "Yes" : "No"}</td>
                                        <td>{plan.isActive ? "Active" : "Inactive"}</td>
                                        <td>
                                            <button
                                                className="btn btn-primary btn-sm me-2"
                                                onClick={() => editPlan(plan)}
                                            >
                                                Update
                                            </button>

                                            <button
                                                className={
                                                    plan.isActive
                                                        ? "btn btn-danger btn-sm"
                                                        : "btn btn-success btn-sm"
                                                }
                                                onClick={() => togglePlanStatus(plan.id)}
                                            >
                                                {plan.isActive ? "Deactivate" : "Activate"}
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default InsurancePlanManagement;