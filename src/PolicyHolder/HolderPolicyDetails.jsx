import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router"
import axios from "axios"
import HolderNavbar from "./HolderNavbar"

function HolderPolicyDetails() {
  const { id } = useParams()
  const navigate = useNavigate()

  const [policy, setPolicy] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState("")

  const token = localStorage.getItem("token")

  const config = {
    headers: {
      Authorization: "Bearer " + token,
    },
  }

  useEffect(() => {
    const getPolicy = async () => {
      try {
        const response = await axios.get(
          "http://localhost:8080/api/policy/get-one/" + id,
          config
        )
        setPolicy(response.data)
      } catch (err) {
        console.log(err)
        if (err.response?.status === 401) {
          setError("Unauthorized. Please login again.")
        } else if (err.response?.status === 403) {
          setError("You do not have permission to view this policy.")
        } else if (err.response?.status === 404) {
          setError("Policy not found.")
        } else {
          setError("Unable to load this policy.")
        }
      } finally {
        setLoading(false)
      }
    }

    getPolicy()
  }, [id])

  if (loading) {
    return (
      <div className="min-vh-100 bg-light">
        <HolderNavbar />
        <div className="container text-center py-5">
          <div className="spinner-border text-primary"></div>
          <p className="text-muted mt-3">Loading policy...</p>
        </div>
      </div>
    )
  }

  if (error) {
    return (
      <div className="min-vh-100 bg-light">
        <HolderNavbar />
        <div className="container py-5">
          <div className="card border-0 shadow text-center p-5">
            <h4 className="text-danger">Unable to Load Policy</h4>
            <p className="text-muted">{error}</p>
            <button
              className="btn btn-primary"
              onClick={() => navigate("/holder/policies")}
            >
              Back to My Policies
            </button>
          </div>
        </div>
      </div>
    )
  }

  if (!policy) {
    return (
      <div className="min-vh-100 bg-light">
        <HolderNavbar />
        <div className="container text-center py-5">
          <h5>Policy not found</h5>
          <button
            className="btn btn-primary mt-3"
            onClick={() => navigate("/holder/policies")}
          >
            Back to Policies
          </button>
        </div>
      </div>
    )
  }

  return (
    <div className="min-vh-100 bg-light">
      <HolderNavbar />

      <div className="container py-5">
        <button
          className="btn btn-outline-primary mb-4"
          onClick={() => navigate("/holder/policies")}
        >
          ← Back to My Policies
        </button>

        <div
          className="card border-0 shadow mx-auto"
          style={{ maxWidth: "850px" }}
        >
          <div className="card-header bg-white p-4">
            <div className="d-flex justify-content-between">
              <div>
                <h3 className="fw-bold text-primary mb-1">
                  EliteDrive Insurance
                </h3>
                <small className="text-muted">Vehicle Insurance Policy</small>
              </div>

              <div className="text-end">
                <strong>POLICY DOCUMENT</strong>
                <br />
                <small className="text-muted">
                  Policy No: {policy.policyNumber}
                </small>
              </div>
            </div>
          </div>

          <div className="card-body p-4 p-md-5">
            <div className="bg-light p-3 rounded mb-4">
              <div className="row">
                <div className="col-md-6">
                  <small className="text-muted">Policy Status</small>
                  <br />
                  <strong
                    className={
                      policy.policyStatus === "ACTIVE"
                        ? "text-success"
                        : "text-danger"
                    }
                  >
                    {policy.policyStatus}
                  </strong>
                </div>

                <div className="col-md-6 text-md-end">
                  <small className="text-muted">Policy Number</small>
                  <br />
                  <strong>{policy.policyNumber}</strong>
                </div>
              </div>
            </div>

            <h5 className="fw-bold text-primary mb-3">
              Policy Holder Information
            </h5>

            <div className="row mb-4">
              <div className="col-md-6 mb-3">
                <small className="text-muted">Policy Holder Name</small>
                <br />
                <strong>{policy.policyHolderName || "-"}</strong>
              </div>

              <div className="col-md-6 mb-3">
                <small className="text-muted">Vehicle Number</small>
                <br />
                <strong>{policy.vehicleNumber || "-"}</strong>
              </div>
            </div>

            <hr />

            <h5 className="fw-bold text-primary mb-3">Insurance Details</h5>

            <div className="row">
              <div className="col-md-6 mb-3">
                <small className="text-muted">Insurance Plan</small>
                <br />
                <strong>
                  {policy.planType
                    ? policy.planType.replaceAll("_", " ")
                    : "-"}
                </strong>
              </div>

              <div className="col-md-6 mb-3">
                <small className="text-muted">Coverage Amount</small>
                <br />
                <strong>
                  ₹{" "}
                  {Number(policy.coverageAmount || 0).toLocaleString("en-IN")}
                </strong>
              </div>

              <div className="col-md-6 mb-3">
                <small className="text-muted">Start Date</small>
                <br />
                <strong>
                  {policy.startDate
                    ? new Date(policy.startDate).toLocaleDateString("en-IN")
                    : "-"}
                </strong>
              </div>

              <div className="col-md-6 mb-3">
                <small className="text-muted">End Date</small>
                <br />
                <strong>
                  {policy.endDate
                    ? new Date(policy.endDate).toLocaleDateString("en-IN")
                    : "-"}
                </strong>
              </div>
            </div>

            <hr />

            <h5 className="fw-bold text-primary mb-3">Premium Details</h5>

            <div className="bg-light p-3 rounded">
              <div className="d-flex justify-content-between">
                <strong>Total Premium</strong>
                <strong className="text-primary fs-5">
                  ₹{" "}
                  {Number(policy.premiumAmount || 0).toLocaleString("en-IN", {
                    minimumFractionDigits: 2,
                  })}
                </strong>
              </div>
            </div>

            <div className="text-center mt-5 pt-4 border-top">
              <small className="text-muted">
                This is a digitally generated insurance policy document.
              </small>
              <br />
              <small className="text-muted">
                Please retain this document for your records.
              </small>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default HolderPolicyDetails