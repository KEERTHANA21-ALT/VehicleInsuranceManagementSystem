
import { useEffect, useState } from "react"
import axios from "axios"
import { useLocation } from "react-router"
import HolderNavbar from "./HolderNavbar"

function HolderClaim() {

    const location = useLocation()

    const [policies, setPolicies] = useState([])
    const [claims, setClaims] = useState([])

    const [policyId, setPolicyId] = useState("")
    const [selectedPolicy, setSelectedPolicy] = useState(null)

    const [claimReason, setClaimReason] = useState("")
    const [claimRemarks, setClaimRemarks] = useState("")
    const [proof, setProof] = useState(null)

    const [loading, setLoading] = useState(true)
    const [submitting, setSubmitting] = useState(false)

    const token = localStorage.getItem("token")

    const config = {
        headers: {
            Authorization: "Bearer " + token
        }
    }


    // ================================
    // GET POLICIES AND CLAIMS
    // ================================

    useEffect(() => {

        const getData = async () => {

            try {

                const policyResponse = await axios.get(
                    "http://localhost:8080/api/policy/get-myPolicies",
                    config
                )

                setPolicies(policyResponse.data)


                // If coming from My Policies -> Raise Claim
                if (location.state?.policy) {

                    const policy = location.state.policy

                    setPolicyId(policy.id)
                    setSelectedPolicy(policy)

                }


                const claimResponse = await axios.get(
                    "http://localhost:8080/api/claim/get-my-claims",
                    config
                )

                if (Array.isArray(claimResponse.data)) {
                    setClaims(claimResponse.data)
                } else {
                    setClaims([])
                }

            } catch (error) {

                console.log(error)
                alert("Unable to load claims")

            } finally {

                setLoading(false)

            }

        }

        getData()

    }, [])


    // ================================
    // SELECT POLICY
    // ================================

    const handlePolicyChange = (e) => {

        const id = e.target.value

        setPolicyId(id)

        if (!id) {

            setSelectedPolicy(null)
            return

        }

        const policy = policies.find(
            p => String(p.id) === String(id)
        )

        setSelectedPolicy(policy || null)

    }


    // ================================
    // RAISE CLAIM
    // ================================

    const raiseClaim = async (e) => {

        e.preventDefault()


        // Validate policy
        if (!policyId) {

            alert("Please select a policy")
            return

        }


        // Validate claim reason
        if (!claimReason) {

            alert("Please select claim reason")
            return

        }


        // Validate description
        if (!claimRemarks.trim()) {

            alert("Please enter claim description")
            return

        }


        // Validate image
        if (!proof) {

            alert("Please upload supporting proof")
            return

        }


        try {

            setSubmitting(true)


            // ================================
            // CREATE CLAIM
            // ================================

            const claimData = {

                policyId: Number(policyId),

                claimReason: claimReason,

                claimRemarks: claimRemarks

            }


            const response = await axios.post(
                "http://localhost:8080/api/claim/add",
                claimData,
                config
            )


            // Created claim ID
            const claimId = response.data.id


            // ================================
            // UPLOAD IMAGE
            // ================================

            const formData = new FormData()

            formData.append("cImage", proof)


            await axios.post(
                `http://localhost:8080/api/claim/image/upload/${claimId}`,
                formData,
                {
                    headers: {
                        Authorization: "Bearer " + token,
                        "Content-Type": "multipart/form-data"
                    }
                }
            )


            // ================================
            // REFRESH CLAIMS
            // ================================

            const claimResponse = await axios.get(
                "http://localhost:8080/api/claim/get-my-claims",
                config
            )

            if (Array.isArray(claimResponse.data)) {

                setClaims(claimResponse.data)

            }


            // ================================
            // CLEAR FORM
            // ================================

            setPolicyId("")
            setSelectedPolicy(null)
            setClaimReason("")
            setClaimRemarks("")
            setProof(null)


            alert(
                "Claim raised successfully. Your claim has been submitted for review."
            )

        } catch (error) {

            console.log(error)

            if (error.response) {

                console.log(
                    "Backend Error:",
                    error.response.data
                )

            }

            alert("Unable to raise claim")

        } finally {

            setSubmitting(false)

        }

    }


    // ================================
    // LOADING
    // ================================

    if (loading) {

        return (

            <>

                <HolderNavbar />

                <div className="container mt-5 text-center">

                    <h5>Loading...</h5>

                </div>

            </>

        )

    }


    return (

        <div
            className="min-vh-100"
            style={{
                backgroundColor: "#f4f7fb"
            }}
        >

            <HolderNavbar />


            <div className="container py-5">


                {/* HEADER */}

                <div className="mb-4">

                    <h2
                        className="fw-bold"
                        style={{
                            color: "#123b6d"
                        }}
                    >
                        Claims
                    </h2>

                    <p className="text-muted">
                        Raise a claim and track your claim status.
                    </p>

                </div>


                {/* ================================
                    RAISE CLAIM
                ================================= */}

                <div
                    className="card border-0 shadow-sm mb-5"
                    style={{
                        borderRadius: "14px"
                    }}
                >

                    <div className="card-body p-4">


                        <h5
                            className="fw-bold mb-1"
                            style={{
                                color: "#123b6d"
                            }}
                        >
                            Raise a Claim
                        </h5>

                        <p className="text-muted small mb-4">
                            Select your policy and provide the claim details.
                        </p>


                        <form onSubmit={raiseClaim}>


                            {/* POLICY */}

                            <div className="mb-4">

                                <label className="form-label fw-semibold">
                                    Select Policy
                                </label>

                                <select
                                    className="form-select"
                                    value={policyId}
                                    onChange={handlePolicyChange}
                                >

                                    <option value="">
                                        Select your policy
                                    </option>

                                    {policies.map((policy) => (

                                        <option
                                            key={policy.id}
                                            value={policy.id}
                                        >

                                            {policy.policyNumber}
                                            {" - "}
                                            {policy.vehicleNumber}

                                        </option>

                                    ))}

                                </select>

                            </div>


                            {/* POLICY DETAILS */}

                            {selectedPolicy && (

                                <div
                                    className="p-4 mb-4"
                                    style={{
                                        backgroundColor: "#f8fafc",
                                        border: "1px solid #dfe5eb",
                                        borderRadius: "12px"
                                    }}
                                >

                                    <h6
                                        className="fw-bold mb-3"
                                        style={{
                                            color: "#123b6d"
                                        }}
                                    >
                                        Policy Details
                                    </h6>


                                    <div className="row g-3">


                                        <div className="col-md-4">

                                            <small className="text-muted">
                                                Policy Number
                                            </small>

                                            <div className="fw-semibold mt-1">
                                                {selectedPolicy.policyNumber}
                                            </div>

                                        </div>


                                        <div className="col-md-4">

                                            <small className="text-muted">
                                                Vehicle Number
                                            </small>

                                            <div className="fw-semibold mt-1">
                                                {selectedPolicy.vehicleNumber}
                                            </div>

                                        </div>


                                        <div className="col-md-4">

                                            <small className="text-muted">
                                                Insurance Plan
                                            </small>

                                            <div className="fw-semibold mt-1">
                                                {selectedPolicy.planType}
                                            </div>

                                        </div>


                                        <div className="col-md-4">

                                            <small className="text-muted">
                                                Start Date
                                            </small>

                                            <div className="fw-semibold mt-1">
                                                {selectedPolicy.startDate}
                                            </div>

                                        </div>


                                        <div className="col-md-4">

                                            <small className="text-muted">
                                                End Date
                                            </small>

                                            <div className="fw-semibold mt-1">
                                                {selectedPolicy.endDate}
                                            </div>

                                        </div>


                                        <div className="col-md-4">

                                            <small className="text-muted">
                                                Premium
                                            </small>

                                            <div className="fw-semibold mt-1">
                                                ₹ {selectedPolicy.premiumAmount}
                                            </div>

                                        </div>

                                    </div>

                                </div>

                            )}


                            {/* CLAIM REASON */}

                            <div className="mb-3">

                                <label className="form-label fw-semibold">
                                    Claim Reason
                                    <span className="text-danger"> *</span>
                                </label>

                                <select
                                    className="form-select"
                                    value={claimReason}
                                    onChange={(e) =>
                                        setClaimReason(e.target.value)
                                    }
                                >

                                    <option value="">
                                        Select claim reason
                                    </option>

                                    <option value="ACCIDENT">
                                        Accident
                                    </option>

                                    <option value="THEFT">
                                        Theft
                                    </option>

                                    <option value="VEHICLE_DAMAGE">
                                        Vehicle Damage
                                    </option>

                                    <option value="NATURAL_DISASTER">
                                        Natural Disaster
                                    </option>

                                    <option value="OTHER">
                                        Other
                                    </option>

                                </select>

                            </div>


                            {/* CLAIM DESCRIPTION */}

                            <div className="mb-3">

                                <label className="form-label fw-semibold">
                                    Claim Description
                                    <span className="text-danger"> *</span>
                                </label>

                                <textarea
                                    className="form-control"
                                    rows="4"
                                    placeholder="Describe what happened in detail..."
                                    value={claimRemarks}
                                    onChange={(e) =>
                                        setClaimRemarks(e.target.value)
                                    }
                                />

                                <small className="text-muted">
                                    Provide details about the accident,
                                    damage, theft, or loss.
                                </small>

                            </div>


                            {/* SUPPORTING PROOF */}

                            <div className="mb-4">

                                <label className="form-label fw-semibold">
                                    Supporting Proof
                                    <span className="text-danger"> *</span>
                                </label>

                                <input
                                    type="file"
                                    className="form-control"
                                    accept="image/*"
                                    required
                                    onChange={(e) =>
                                        setProof(e.target.files[0])
                                    }
                                />

                                <small className="text-muted">
                                    Upload accident photos, vehicle damage
                                    images, theft proof, or other supporting
                                    evidence.
                                </small>


                                {proof && (

                                    <div className="mt-2 text-success small">

                                        <i className="bi bi-check-circle me-1"></i>

                                        {proof.name}

                                    </div>

                                )}

                            </div>


                            {/* SUBMIT BUTTON */}

                            <button
                                type="submit"
                                className="btn w-100 py-2 fw-semibold"
                                disabled={
                                    submitting ||
                                    !selectedPolicy ||
                                    !claimReason ||
                                    !claimRemarks.trim() ||
                                    !proof
                                }
                                style={{
                                    backgroundColor:
                                        selectedPolicy &&
                                        claimReason &&
                                        claimRemarks.trim() &&
                                        proof
                                            ? "#1261a0"
                                            : "#adb5bd",

                                    color: "white",

                                    border: "none",

                                    borderRadius: "8px"
                                }}
                            >

                                {submitting
                                    ? "Submitting Claim..."
                                    : "Raise Claim"}

                            </button>

                        </form>

                    </div>

                </div>


                {/* ================================
                    MY CLAIMS
                ================================= */}

                <div>

                    <div className="mb-3">

                        <h4
                            className="fw-bold"
                            style={{
                                color: "#123b6d"
                            }}
                        >
                            My Claims
                        </h4>

                        <p className="text-muted">
                            View and track all your claims.
                        </p>

                    </div>


                    {claims.length === 0 ? (

                        <div
                            className="bg-white p-5 text-center shadow-sm"
                            style={{
                                borderRadius: "14px"
                            }}
                        >

                            <i
                                className="bi bi-clipboard-x"
                                style={{
                                    fontSize: "40px",
                                    color: "#adb5bd"
                                }}
                            ></i>

                            <h5 className="fw-semibold mt-3">
                                No Claims Yet
                            </h5>

                            <p className="text-muted mb-0">
                                Your raised claims will appear here.
                            </p>

                        </div>

                    ) : (

                        <div
                            className="card border-0 shadow-sm"
                            style={{
                                borderRadius: "14px"
                            }}
                        >

                            <div className="card-body p-0">

                                {claims.map((claim, index) => (

                                    <div
                                        key={claim.id}
                                        className="p-4"
                                        style={{
                                            borderBottom:
                                                index !== claims.length - 1
                                                    ? "1px solid #e9edf2"
                                                    : "none"
                                        }}
                                    >

                                        <div className="row align-items-center">


                                            {/* CLAIM DETAILS */}

                                            <div className="col-md-8">

                                                <h6
                                                    className="fw-bold mb-2"
                                                    style={{
                                                        color: "#123b6d"
                                                    }}
                                                >
                                                    Claim #{claim.id}
                                                </h6>


                                                <p className="mb-1">

                                                    <strong>
                                                        Reason:
                                                    </strong>{" "}

                                                    {claim.claimReason}

                                                </p>


                                                <p className="text-muted mb-1">

                                                    <strong>
                                                        Description:
                                                    </strong>{" "}

                                                    {claim.claimRemarks}

                                                </p>


                                                {claim.claimDate && (

                                                    <small className="text-muted">

                                                        Submitted:{" "}

                                                        {new Date(
                                                            claim.claimDate
                                                        ).toLocaleDateString()}

                                                    </small>

                                                )}

                                            </div>


                                            {/* STATUS */}

                                            <div className="col-md-4 text-md-end mt-3 mt-md-0">

                                                <span
                                                    className="badge px-3 py-2"
                                                    style={{

                                                        backgroundColor:
                                                            claim.claimStatus === "SUBMITTED"
                                                                ? "#fff3cd"
                                                                : claim.claimStatus === "UNDER_REVIEW"
                                                                    ? "#cfe2ff"
                                                                    : claim.claimStatus === "APPROVED"
                                                                        ? "#d1e7dd"
                                                                        : claim.claimStatus === "REJECTED"
                                                                            ? "#f8d7da"
                                                                            : claim.claimStatus === "PAID"
                                                                                ? "#d1e7dd"
                                                                                : "#e2e3e5",

                                                        color:
                                                            claim.claimStatus === "SUBMITTED"
                                                                ? "#856404"
                                                                : claim.claimStatus === "UNDER_REVIEW"
                                                                    ? "#084298"
                                                                    : claim.claimStatus === "APPROVED"
                                                                        ? "#0f5132"
                                                                        : claim.claimStatus === "REJECTED"
                                                                            ? "#842029"
                                                                            : claim.claimStatus === "PAID"
                                                                                ? "#0f5132"
                                                                                : "#41464b"

                                                    }}
                                                >

                                                    {claim.claimStatus || "SUBMITTED"}

                                                </span>

                                            </div>

                                        </div>

                                    </div>

                                ))}

                            </div>

                        </div>

                    )}

                </div>

            </div>

        </div>

    )
}

export default HolderClaim

