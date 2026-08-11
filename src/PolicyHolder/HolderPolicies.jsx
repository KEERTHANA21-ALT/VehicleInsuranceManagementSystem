
import { useEffect, useState } from "react"
import { useNavigate } from "react-router"
import axios from "axios"
import HolderNavbar from "./HolderNavbar"

function HolderPolicies() {

    const navigate = useNavigate()

    const [policies, setPolicies] = useState([])
    const [claims, setClaims] = useState([])

    const [loading, setLoading] = useState(true)

    const token = localStorage.getItem("token")

    const config = {
        headers: {
            Authorization: "Bearer " + token,
        },
    }


    useEffect(() => {

        const getPolicies = async () => {

            try {

                // Get user's policies

                const response = await axios.get(
                    "http://localhost:8080/api/policy/get-myPolicies",
                    config
                )

                const policyList = response.data

                setPolicies(policyList)


                // Get claims for each policy

                const claimList = []

                for (let i = 0; i < policyList.length; i++) {

                    const policy = policyList[i]

                    try {

                        const claimResponse = await axios.get(
                            "http://localhost:8080/api/claim/get-by-policy/"
                            + policy.id,
                            config
                        )

                        if (
                            claimResponse.data &&
                            claimResponse.data.length > 0
                        ) {

                            claimList.push(
                                ...claimResponse.data
                            )

                        }

                    } catch (error) {

                        console.log(
                            "No claim for policy:",
                            policy.id
                        )

                    }

                }

                setClaims(claimList)

            } catch (error) {

                console.log(error)

                alert("Unable to load policies")

            } finally {

                setLoading(false)

            }

        }

        getPolicies()

    }, [])


    // Find claim belonging to a policy

    const getPolicyClaim = (policyId) => {

        for (let i = 0; i < claims.length; i++) {

            if (claims[i].policyId == policyId) {

                return claims[i]

            }

        }

        return null

    }


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

                <h2
                    className="fw-bold"
                    style={{
                        color: "#123b6d"
                    }}
                >
                    My Policies
                </h2>

                <p className="text-muted">
                    View all your vehicle insurance policies.
                </p>


                {policies.length === 0 ? (

                    <div className="bg-white p-5 text-center rounded shadow-sm">

                        <h5>
                            No Policies Yet
                        </h5>

                        <p className="text-muted">
                            Your policies will appear here.
                        </p>

                    </div>

                ) : (

                    <div className="bg-white rounded shadow-sm">

                        {policies.map((policy, index) => {

                            const claim = getPolicyClaim(policy.id)

                            return (

                                <div
                                    key={policy.id}
                                    className="p-4"
                                    style={{
                                        borderBottom:
                                            index !== policies.length - 1
                                                ? "1px solid #ddd"
                                                : "none",
                                    }}
                                >

                                    <div className="row align-items-center">


                                        {/* POLICY DETAILS */}

                                        <div className="col-md-7">

                                            <h5
                                                className="fw-bold"
                                                style={{
                                                    color: "#123b6d"
                                                }}
                                            >
                                                {policy.policyNumber}
                                            </h5>

                                            <p className="mb-1">
                                                Vehicle: {policy.vehicleNumber}
                                            </p>

                                            <p className="text-muted">
                                                Plan: {policy.planType}
                                            </p>


                                            {/* CLAIM STATUS */}

                                            {claim && (

                                                <div className="mt-3">

                                                    <span
                                                        className="badge px-3 py-2"
                                                        style={{
                                                            backgroundColor:
                                                                claim.claimStatus === "APPROVED" ||
                                                                claim.claimStatus === "PAID"
                                                                    ? "#198754"
                                                                    : claim.claimStatus === "REJECTED"
                                                                        ? "#dc3545"
                                                                        : "#ffc107",

                                                            color:
                                                                claim.claimStatus === "SUBMITTED" ||
                                                                claim.claimStatus === "UNDER_REVIEW"
                                                                    ? "#212529"
                                                                    : "white"
                                                        }}
                                                    >
                                                        Claim Status:{" "}
                                                        {claim.claimStatus || "SUBMITTED"}
                                                    </span>

                                                </div>

                                            )}

                                        </div>


                                        {/* POLICY INFORMATION */}

                                        <div className="col-md-3">

                                            <p className="mb-1">
                                                Start Date: {policy.startDate}
                                            </p>

                                            <p className="mb-1">
                                                End Date: {policy.endDate}
                                            </p>

                                            <p>
                                                Premium: ₹{policy.premiumAmount}
                                            </p>

                                        </div>


                                        {/* ACTIONS */}

                                        <div className="col-md-2">


                                            {/* VIEW POLICY */}

                                            <button
                                                className="btn btn-primary w-100 mb-2"
                                                onClick={() =>
                                                    navigate(
                                                        "/holder/policies/"
                                                        + policy.id
                                                    )
                                                }
                                            >
                                                View Policy
                                            </button>


                                            {/* CLAIM ACTION */}

                                            {!claim ? (

                                                <button
                                                    className="btn btn-outline-danger w-100"
                                                    onClick={() =>
                                                        navigate(
                                                            "/holder/claims/"
                                                            + policy.id
                                                        )
                                                    }
                                                >
                                                    Raise Claim
                                                </button>

                                            ) : (

                                                <div className="text-center">

                                                    {claim.claimStatus === "PAID" ? (

                                                        <div
                                                            className="text-success fw-semibold"
                                                            style={{
                                                                fontSize: "14px"
                                                            }}
                                                        >
                                                            ✓ Claim Paid
                                                        </div>

                                                    ) : claim.claimStatus === "REJECTED" ? (

                                                        <div
                                                            className="text-danger fw-semibold"
                                                            style={{
                                                                fontSize: "14px"
                                                            }}
                                                        >
                                                            ✕ Claim Rejected
                                                        </div>

                                                    ) : (

                                                        <div
                                                            className="text-success fw-semibold"
                                                            style={{
                                                                fontSize: "14px"
                                                            }}
                                                        >
                                                            ✓ Claim Raised Successfully
                                                        </div>

                                                    )}

                                                </div>

                                            )}

                                        </div>

                                    </div>

                                </div>

                            )

                        })}

                    </div>

                )}

            </div>

        </div>
    )
}

export default HolderPolicies
