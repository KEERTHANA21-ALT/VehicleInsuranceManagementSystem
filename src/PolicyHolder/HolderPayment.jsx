import { useEffect, useState } from "react"
import axios from "axios"
import { useNavigate } from "react-router"
import HolderNavbar from "./HolderNavbar"

function HolderPayment() {
    const navigate = useNavigate()

    const [proposal, setProposal] = useState(null)
    const [paymentMethod, setPaymentMethod] = useState("")
    const [paymentStatus, setPaymentStatus] = useState("PENDING")

    const token = localStorage.getItem("token")
    const proposalId = localStorage.getItem("proposalId")

    useEffect(() => {
        axios.get("http://localhost:8080/api/proposal/get-myProposals", {
            headers: {
                Authorization: "Bearer " + token,
            },
        })
            .then((response) => {
                const proposals = response.data
                for (let i = 0; i < proposals.length; i++) {
                    if (proposals[i].id == proposalId) {
                        setProposal(proposals[i])
                        break
                    }
                }
            })
            .catch((error) => {
                console.log(error)
                alert("Unable to load proposal")
            })
    }, [])

    const handlePayment = async () => {
        if (paymentMethod === "") {
            alert("Please select payment method")
            return
        }

        try {
            const response = await axios.post(
                "http://localhost:8080/api/payment/add/" + proposalId,
                {
                    paymentMethod: paymentMethod,
                },
                {
                    headers: {
                        Authorization: "Bearer " + token,
                    },
                }
            )

            const paymentId = response.data.id

            await axios.put(
                "http://localhost:8080/api/payment/update/" + paymentId,
                {},
                {
                    headers: {
                        Authorization: "Bearer " + token,
                    },
                    params: {
                        paymentStatus: "SUCCESS",
                    },
                }
            )

            setPaymentStatus("SUCCESS")
            alert("Payment successful!")
        } catch (error) {
            console.log(error)
            setPaymentStatus("FAILED")
            alert("Payment failed")
        }
    }

    if (proposal === null) {
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
        <div className="min-vh-100" style={{ backgroundColor: "#f5f7fa" }}>
            <HolderNavbar />

            <div className="container py-5">
                <h2 className="fw-bold" style={{ color: "#123b6d" }}>
                    Make Payment
                </h2>

                <p className="text-muted">Complete your insurance payment.</p>

                <div className="row g-4">
                    <div className="col-lg-7">
                        <div className="bg-white p-4 rounded shadow-sm">
                            <h5 className="fw-bold mb-3">Select Payment Method</h5>

                            <div className="form-check mb-3">
                                <input
                                    className="form-check-input"
                                    type="radio"
                                    name="payment"
                                    value="UPI"
                                    checked={paymentMethod === "UPI"}
                                    onChange={(e) => setPaymentMethod(e.target.value)}
                                />
                                <label className="form-check-label">UPI</label>
                            </div>

                            <div className="form-check mb-3">
                                <input
                                    className="form-check-input"
                                    type="radio"
                                    name="payment"
                                    value="CREDIT_CARD"
                                    checked={paymentMethod === "CREDIT_CARD"}
                                    onChange={(e) => setPaymentMethod(e.target.value)}
                                />
                                <label className="form-check-label">Credit Card</label>
                            </div>

                            <div className="form-check mb-3">
                                <input
                                    className="form-check-input"
                                    type="radio"
                                    name="payment"
                                    value="DEBIT_CARD"
                                    checked={paymentMethod === "DEBIT_CARD"}
                                    onChange={(e) => setPaymentMethod(e.target.value)}
                                />
                                <label className="form-check-label">Debit Card</label>
                            </div>

                            <div className="form-check mb-4">
                                <input
                                    className="form-check-input"
                                    type="radio"
                                    name="payment"
                                    value="NET_BANKING"
                                    checked={paymentMethod === "NET_BANKING"}
                                    onChange={(e) => setPaymentMethod(e.target.value)}
                                />
                                <label className="form-check-label">Net Banking</label>
                            </div>

                            {paymentStatus === "SUCCESS" && (
                                <div className="alert alert-success">Payment Successful!</div>
                            )}

                            {paymentStatus === "FAILED" && (
                                <div className="alert alert-danger">Payment Failed!</div>
                            )}

                            <div className="d-flex gap-2">
                                <button
                                    className="btn btn-secondary"
                                    onClick={() => navigate("/holder/proposals")}
                                >
                                    Cancel
                                </button>

                                <button
                                    className="btn btn-primary"
                                    onClick={handlePayment}
                                    disabled={paymentMethod === "" || paymentStatus === "SUCCESS"}
                                >
                                    {paymentStatus === "SUCCESS" ? "Payment Completed" : "Pay Now"}
                                </button>
                            </div>
                        </div>
                    </div>

                    <div className="col-lg-5">
                        <div className="bg-white p-4 rounded shadow-sm">
                            <h5 className="fw-bold">Proposal #{proposal.id}</h5>
                            <hr />

                            <div className="d-flex justify-content-between mb-3">
                                <span>Base Premium</span>
                                <span>₹ {proposal.basePremium}</span>
                            </div>

                            <div className="d-flex justify-content-between mb-3">
                                <span>Discount</span>
                                <span className="text-success">- ₹ {proposal.discount}</span>
                            </div>

                            <hr />

                            <div className="d-flex justify-content-between">
                                <strong>Amount to Pay</strong>
                                <strong>₹ {proposal.premiumAmount}</strong>
                            </div>

                            <div className="mt-4">
                                <div className="d-flex justify-content-between">
                                    <span>Payment Status</span>
                                    <strong
                                        className={
                                            paymentStatus === "SUCCESS"
                                                ? "text-success"
                                                : paymentStatus === "FAILED"
                                                    ? "text-danger"
                                                    : "text-warning"
                                        }
                                    >
                                        {paymentStatus}
                                    </strong>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default HolderPayment