
import { useEffect, useState } from "react"
import axios from "axios"
import { useNavigate, useParams } from "react-router"
import HolderNavbar from "./HolderNavbar"

function HolderCreateProposal() {

    const navigate = useNavigate()
    const { id } = useParams()

    const [vehicle, setVehicle] = useState(null)
    const [plans, setPlans] = useState([])
    const [addons, setAddons] = useState([])

    const [selectedPlan, setSelectedPlan] = useState(null)
    const [selectedPlanData, setSelectedPlanData] = useState(null)
    const [selectedAddons, setSelectedAddons] = useState([])

    // Insurance Plan Pagination
    const [planPage, setPlanPage] = useState(0)
    const [planSize] = useState(3)

    // Addon Pagination
    const [addonPage, setAddonPage] = useState(0)
    const [addonSize] = useState(3)
    const [addonNext, setAddonNext] = useState(false)

    const [loading, setLoading] = useState(false)

    const token = localStorage.getItem("token")

    const config = {
        headers: {
            Authorization: "Bearer " + token
        }
    }


    // =========================================================
    // GET VEHICLE
    // Only when vehicle id exists
    // =========================================================

    useEffect(() => {

        if (!id) {
            return
        }

        const getVehicle = async () => {

            try {

                const response = await axios.get(
                    "http://localhost:8080/api/vehicle/" + id,
                    config
                )

                setVehicle(response.data)

            } catch (error) {

                console.log(error)

            }

        }

        getVehicle()

    }, [id])


    // =========================================================
    // GET ALL INSURANCE PLANS
    // Client-side pagination: 3 cards per page
    // =========================================================

    useEffect(() => {

        const getPlans = async () => {

            try {

                const response = await axios.get(
                    "http://localhost:8080/api/insurancePlan/get-all",
                    config
                )

                setPlans(response.data)

            } catch (error) {

                console.log(error)

            }

        }

        getPlans()

    }, [])


    // =========================================================
    // GET ADDONS
    // Server-side pagination: 3 cards per page
    // =========================================================

    useEffect(() => {

        const getAddons = async () => {

            try {

                const response = await axios.get(
                    "http://localhost:8080/api/addon/get-all?page="
                    + addonPage
                    + "&size="
                    + addonSize,
                    config
                )

                setAddons(response.data)

                if (response.data.length === addonSize) {
                    setAddonNext(true)
                } else {
                    setAddonNext(false)
                }

            } catch (error) {

                console.log(error)

            }

        }

        getAddons()

    }, [addonPage])


    // =========================================================
    // INSURANCE PLANS TO DISPLAY
    // ALWAYS ONLY 3 CARDS
    // =========================================================

    const startPlanIndex = planPage * planSize

    const displayedPlans = plans.slice(
        startPlanIndex,
        startPlanIndex + planSize
    )

    const planNext =
        startPlanIndex + planSize < plans.length


    // =========================================================
    // SELECT PLAN
    // =========================================================

    const selectPlan = (plan) => {

        setSelectedPlan(plan.id)
        setSelectedPlanData(plan)

    }


    // =========================================================
    // SELECT ADDON
    // =========================================================

    const selectAddon = (addon) => {

        let found = false

        for (let i = 0; i < selectedAddons.length; i++) {

            if (selectedAddons[i].id === addon.id) {
                found = true
            }

        }

        if (found) {

            const newAddons = []

            for (let i = 0; i < selectedAddons.length; i++) {

                if (selectedAddons[i].id !== addon.id) {
                    newAddons.push(selectedAddons[i])
                }

            }

            setSelectedAddons(newAddons)

        } else {

            setSelectedAddons([
                ...selectedAddons,
                addon
            ])

        }

    }


    // =========================================================
    // PREMIUM CALCULATION
    // Only relevant when vehicle proposal is being created
    // =========================================================

    let basePremium = 0
    let discount = 0
    let addonAmount = 0

    if (selectedPlanData) {

        basePremium =
            Number(selectedPlanData.basePremium)

        discount =
            basePremium *
            Number(selectedPlanData.discountPercentage || 0) /
            100

    }

    for (let i = 0; i < selectedAddons.length; i++) {

        addonAmount =
            addonAmount +
            Number(selectedAddons[i].price)

    }

    const finalAmount =
        basePremium -
        discount +
        addonAmount


    // =========================================================
    // CREATE PROPOSAL
    // =========================================================

    const createProposal = async () => {

        if (!selectedPlan) {

            alert("Please select an insurance plan")

            return

        }

        if (!id) {

            alert("Please select a vehicle first")

            navigate("/holder/vehicles")

            return

        }

        try {

            setLoading(true)

            const proposalData = {
                vehicleId: id,
                insurancePlanId: selectedPlan
            }

            const response = await axios.post(
                "http://localhost:8080/api/proposal/add",
                proposalData,
                config
            )

            const proposalId = response.data.id


            // Add selected addons

            for (let i = 0; i < selectedAddons.length; i++) {

                await axios.post(
                    "http://localhost:8080/api/proposal-addon/add/"
                    + proposalId
                    + "/"
                    + selectedAddons[i].id,
                    {},
                    config
                )

            }

            alert("Proposal created successfully")

            navigate("/holder/proposals")

        } catch (error) {

            console.log(error)

            alert("Proposal creation failed")

        } finally {

            setLoading(false)

        }

    }


    return (

        <div
            className="min-vh-100"
            style={{
                backgroundColor: "#f4f7fb"
            }}
        >

            <HolderNavbar />


            <div className="container py-4 py-md-5">


                {/* =================================================
                    PAGE HEADER
                ================================================= */}

                <div className="mb-4">

                    <h2
                        className="fw-bold mb-1"
                        style={{
                            color: "#123b6d"
                        }}
                    >
                        {id
                            ? "Create Insurance Proposal"
                            : "Insurance Plans"}
                    </h2>

                    <p className="text-muted mb-0">

                        {id
                            ? "Select an insurance plan and additional coverage for your vehicle."
                            : "Choose an insurance plan and additional coverage that suits your needs."}

                    </p>

                </div>


                {/* =================================================
                    VEHICLE DETAILS
                    ONLY WHEN ID EXISTS
                ================================================= */}

                {id && (

                    <div
                        className="card border-0 shadow-sm mb-4"
                        style={{
                            borderRadius: "14px"
                        }}
                    >

                        <div className="card-body p-4">

                            <h5
                                className="fw-bold mb-3"
                                style={{
                                    color: "#123b6d"
                                }}
                            >
                                Vehicle Details
                            </h5>


                            {vehicle ? (

                                <div
                                    className="p-3"
                                    style={{
                                        backgroundColor: "#f8fafc",
                                        border: "1px solid #e2e8f0",
                                        borderRadius: "10px"
                                    }}
                                >

                                    <div className="row g-3">

                                        <div className="col-md-3">

                                            <small className="text-muted">
                                                Vehicle
                                            </small>

                                            <div className="fw-semibold mt-1">
                                                {vehicle.vehicleModel}
                                            </div>

                                        </div>


                                        <div className="col-md-3">

                                            <small className="text-muted">
                                                Vehicle Number
                                            </small>

                                            <div className="fw-semibold mt-1">
                                                {vehicle.vehicleNumber}
                                            </div>

                                        </div>


                                        <div className="col-md-3">

                                            <small className="text-muted">
                                                Vehicle Type
                                            </small>

                                            <div className="fw-semibold mt-1">
                                                {vehicle.vehicleType}
                                            </div>

                                        </div>


                                        <div className="col-md-3">

                                            <small className="text-muted">
                                                Year
                                            </small>

                                            <div className="fw-semibold mt-1">
                                                {vehicle.vehicleYear}
                                            </div>

                                        </div>

                                    </div>

                                </div>

                            ) : (

                                <p className="text-muted mb-0">
                                    Loading vehicle details...
                                </p>

                            )}

                        </div>

                    </div>

                )}


                {/* =================================================
                    INSURANCE PLANS
                ================================================= */}

                <div
                    className="card border-0 shadow-sm mb-4"
                    style={{
                        borderRadius: "14px"
                    }}
                >

                    <div className="card-body p-4">

                        <div className="mb-4">

                            <h5
                                className="fw-bold mb-1"
                                style={{
                                    color: "#123b6d"
                                }}
                            >
                                Choose Insurance Plan
                            </h5>

                            <p className="text-muted small mb-0">
                                Select the plan that best suits your vehicle.
                            </p>

                        </div>


                        {/* ALWAYS ONLY 3 CARDS */}

                        <div className="row g-3">

                            {displayedPlans.map((plan) => (

                                <div
                                    className="col-lg-4 col-md-6"
                                    key={plan.id}
                                >

                                    <div
                                        className="h-100"
                                        onClick={() =>
                                            selectPlan(plan)
                                        }
                                        style={{
                                            cursor: "pointer",
                                            borderRadius: "12px",
                                            border:
                                                selectedPlan === plan.id
                                                    ? "2px solid #1261a0"
                                                    : "1px solid #dfe5eb",
                                            backgroundColor:
                                                selectedPlan === plan.id
                                                    ? "#eef6fd"
                                                    : "white"
                                        }}
                                    >

                                        <div className="p-4">

                                            <div className="d-flex justify-content-between align-items-start">

                                                <div>

                                                    <small
                                                        className="text-uppercase fw-semibold"
                                                        style={{
                                                            color: "#1261a0"
                                                        }}
                                                    >
                                                        Insurance Plan
                                                    </small>

                                                    <h5 className="fw-bold mt-1 mb-3">
                                                        {plan.planType}
                                                    </h5>

                                                </div>


                                                {selectedPlan === plan.id && (

                                                    <span
                                                        className="badge"
                                                        style={{
                                                            backgroundColor: "#1261a0"
                                                        }}
                                                    >
                                                        Selected
                                                    </span>

                                                )}

                                            </div>


                                            <div className="mb-3">

                                                <small className="text-muted">
                                                    Coverage
                                                </small>

                                                <div className="fw-bold fs-5">
                                                    ₹ {plan.coverageAmount}
                                                </div>

                                            </div>


                                            <div className="row">

                                                <div className="col-6">

                                                    <small className="text-muted">
                                                        Premium
                                                    </small>

                                                    <div className="fw-semibold">
                                                        ₹ {plan.basePremium}
                                                    </div>

                                                </div>


                                                <div className="col-6">

                                                    <small className="text-muted">
                                                        Discount
                                                    </small>

                                                    <div className="fw-semibold text-success">
                                                        {plan.discountPercentage}%
                                                    </div>

                                                </div>

                                            </div>

                                        </div>

                                    </div>

                                </div>

                            ))}

                        </div>


                        {/* =================================================
                            INSURANCE PLAN PAGINATION
                            ONLY PREVIOUS | PAGE | NEXT
                        ================================================= */}

                        {plans.length > planSize && (

                            <div
                                className="d-flex justify-content-center align-items-center mt-4 pt-3"
                                style={{
                                    borderTop: "1px solid #e9edf2"
                                }}
                            >

                                <button
                                    className="btn btn-outline-secondary px-4"
                                    disabled={planPage === 0}
                                    onClick={() =>
                                        setPlanPage(planPage - 1)
                                    }
                                >
                                    ← Previous
                                </button>


                                <span
                                    className="mx-4 fw-semibold"
                                    style={{
                                        color: "#123b6d"
                                    }}
                                >
                                    Page {planPage + 1}
                                </span>


                                <button
                                    className="btn btn-outline-primary px-4"
                                    disabled={!planNext}
                                    onClick={() =>
                                        setPlanPage(planPage + 1)
                                    }
                                >
                                    Next →
                                </button>

                            </div>

                        )}

                    </div>

                </div>


                {/* =================================================
                    ADDONS
                ================================================= */}

                <div
                    className="card border-0 shadow-sm mb-4"
                    style={{
                        borderRadius: "14px"
                    }}
                >

                    <div className="card-body p-4">

                        <div className="mb-4">

                            <h5
                                className="fw-bold mb-1"
                                style={{
                                    color: "#123b6d"
                                }}
                            >
                                Additional Coverage
                            </h5>

                            <p className="text-muted small mb-0">
                                Add extra protection to your insurance plan.
                            </p>

                        </div>


                        <div className="row g-3">

                            {addons.map((addon) => {

                                let selected = false

                                for (
                                    let i = 0;
                                    i < selectedAddons.length;
                                    i++
                                ) {

                                    if (
                                        selectedAddons[i].id === addon.id
                                    ) {

                                        selected = true

                                    }

                                }


                                return (

                                    <div
                                        className="col-lg-4 col-md-6"
                                        key={addon.id}
                                    >

                                        <div
                                            className="h-100"
                                            style={{
                                                borderRadius: "12px",
                                                border:
                                                    selected
                                                        ? "2px solid #198754"
                                                        : "1px solid #dfe5eb",
                                                backgroundColor:
                                                    selected
                                                        ? "#f1faf5"
                                                        : "white"
                                            }}
                                        >

                                            <div className="p-4">

                                                <div className="d-flex justify-content-between align-items-start mb-2">

                                                    <h6 className="fw-bold mb-0">
                                                        {addon.name}
                                                    </h6>

                                                    <span
                                                        className="fw-bold"
                                                        style={{
                                                            color: "#1261a0"
                                                        }}
                                                    >
                                                        ₹ {addon.price}
                                                    </span>

                                                </div>


                                                <p className="text-muted small">
                                                    {addon.description}
                                                </p>


                                                <button
                                                    className={
                                                        selected
                                                            ? "btn btn-success w-100"
                                                            : "btn btn-outline-primary w-100"
                                                    }
                                                    onClick={() =>
                                                        selectAddon(addon)
                                                    }
                                                >

                                                    {selected
                                                        ? "✓ Coverage Added"
                                                        : "Add Coverage"}

                                                </button>

                                            </div>

                                        </div>

                                    </div>

                                )

                            })}

                        </div>


                        {/* =================================================
                            ADDON PAGINATION
                            ONLY PREVIOUS | PAGE | NEXT
                        ================================================= */}

                        <div
                            className="d-flex justify-content-center align-items-center mt-4 pt-3"
                            style={{
                                borderTop: "1px solid #e9edf2"
                            }}
                        >

                            <button
                                className="btn btn-outline-secondary px-4"
                                disabled={addonPage === 0}
                                onClick={() =>
                                    setAddonPage(addonPage - 1)
                                }
                            >
                                ← Previous
                            </button>


                            <span
                                className="mx-4 fw-semibold"
                                style={{
                                    color: "#123b6d"
                                }}
                            >
                                Page {addonPage + 1}
                            </span>


                            <button
                                className="btn btn-outline-primary px-4"
                                disabled={!addonNext}
                                onClick={() =>
                                    setAddonPage(addonPage + 1)
                                }
                            >
                                Next →
                            </button>

                        </div>

                    </div>

                </div>


                {/* =================================================
                    PREMIUM SUMMARY
                    ONLY WHEN VEHICLE EXISTS
                ================================================= */}

                {id && (

                    <div
                        className="card border-0 shadow-sm"
                        style={{
                            borderRadius: "14px"
                        }}
                    >

                        <div className="card-body p-4">

                            <h5
                                className="fw-bold mb-4"
                                style={{
                                    color: "#123b6d"
                                }}
                            >
                                Premium Summary
                            </h5>


                            <div className="row align-items-center">

                                <div className="col-md-7">

                                    <div className="d-flex justify-content-between mb-3">

                                        <span className="text-muted">
                                            Base Premium
                                        </span>

                                        <span className="fw-semibold">
                                            ₹ {basePremium}
                                        </span>

                                    </div>


                                    <div className="d-flex justify-content-between mb-3">

                                        <span className="text-muted">
                                            Discount
                                        </span>

                                        <span className="text-success fw-semibold">
                                            − ₹ {discount}
                                        </span>

                                    </div>


                                    <div className="d-flex justify-content-between">

                                        <span className="text-muted">
                                            Add-on Amount
                                        </span>

                                        <span className="fw-semibold">
                                            ₹ {addonAmount}
                                        </span>

                                    </div>

                                </div>


                                <div className="col-md-5 mt-4 mt-md-0">

                                    <div
                                        className="text-center p-4"
                                        style={{
                                            backgroundColor: "#eef6fd",
                                            border: "1px solid #d8eaf8",
                                            borderRadius: "12px"
                                        }}
                                    >

                                        <small className="text-muted">
                                            Total Premium
                                        </small>

                                        <h2
                                            className="fw-bold mb-0 mt-1"
                                            style={{
                                                color: "#1261a0"
                                            }}
                                        >
                                            ₹ {finalAmount}
                                        </h2>

                                    </div>

                                </div>

                            </div>


                            <button
                                className="btn w-100 mt-4 py-3 fw-semibold"
                                style={{
                                    backgroundColor:
                                        selectedPlan && vehicle
                                            ? "#1261a0"
                                            : "#adb5bd",
                                    color: "white",
                                    border: "none",
                                    borderRadius: "8px"
                                }}
                                disabled={
                                    !selectedPlan ||
                                    !vehicle ||
                                    loading
                                }
                                onClick={createProposal}
                            >

                                {loading
                                    ? "Creating Proposal..."
                                    : "Create Proposal"}

                            </button>

                        </div>

                    </div>

                )}

            </div>

        </div>

    )

}

export default HolderCreateProposal

