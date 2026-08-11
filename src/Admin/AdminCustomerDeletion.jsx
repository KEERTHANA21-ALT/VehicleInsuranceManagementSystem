
import { useEffect, useState } from "react";
import axios from "axios";
import AdminNavbar from "./AdminNavbar";

function AdminCustomerDeletion() {

    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [deletingId, setDeletingId] = useState(null);


    // =========================================================
    // FETCH DELETION REQUESTS
    // =========================================================

    const fetchDeletionRequests = async () => {

        try {

            const token = localStorage.getItem("token");

            const response = await axios.get(
                "http://localhost:8080/api/policyHolder/deletion-requests",
                {
                    headers: {
                        Authorization: "Bearer " + token
                    }
                }
            );

            console.log("Deletion requests:", response.data);

            setCustomers(response.data || []);

        } catch (error) {

            console.log(
                "Error fetching deletion requests:",
                error.response?.data || error.message
            );

        } finally {

            setLoading(false);

        }
    };


    // =========================================================
    // FETCH WHEN PAGE LOADS
    // =========================================================

    useEffect(() => {

        fetchDeletionRequests();

    }, []);


    // =========================================================
    // DELETE CUSTOMER
    // =========================================================

    const handleDelete = async (id) => {

        const confirmDelete = window.confirm(
            "Are you sure you want to delete this customer account?"
        );

        if (!confirmDelete) {
            return;
        }


        try {

            setDeletingId(id);

            const token = localStorage.getItem("token");


            await axios.delete(
                `http://localhost:8080/api/policyHolder/delete/${id}`,
                {
                    headers: {
                        Authorization: "Bearer " + token
                    }
                }
            );


            // -------------------------------------------------
            // Update only the selected customer
            // -------------------------------------------------

            setCustomers(prevCustomers =>
                prevCustomers.map(customer =>
                    customer.id === id
                        ? {
                            ...customer,
                            active: false,
                            deletionRequested: false
                        }
                        : customer
                )
            );


            alert("Customer account deleted successfully.");

        } catch (error) {

            console.log(
                "Delete customer error:",
                error.response?.data || error.message
            );

            alert("Failed to delete customer account.");

        } finally {

            setDeletingId(null);

        }

    };


    // =========================================================
    // COUNTS
    // =========================================================

    const pendingCustomers = customers.filter(
        customer =>
            customer.deletionRequested === true &&
            customer.active === true
    );


    const deletedCustomers = customers.filter(
        customer =>
            customer.active === false
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

            <AdminNavbar />


            <div className="container py-5">


                {/* =================================================
                    HEADER
                ================================================= */}

                <div className="mb-4">

                    <h2
                        className="fw-bold mb-1"
                        style={{
                            color: "#123b6d"
                        }}
                    >
                        Customer Deletion Requests
                    </h2>

                    <p className="text-muted mb-0">
                        Review customers who have requested account
                        deletion and manage their account status.
                    </p>

                </div>


                {/* =================================================
                    SUMMARY CARDS
                ================================================= */}

                <div className="row mb-4 g-4">


                    {/* PENDING */}

                    <div className="col-md-4">

                        <div
                            className="card border-0 shadow-sm h-100"
                            style={{
                                borderRadius: "14px"
                            }}
                        >

                            <div className="card-body p-4">

                                <div className="d-flex justify-content-between align-items-center">

                                    <div>

                                        <p className="text-muted mb-2">
                                            Pending Requests
                                        </p>

                                        <h2
                                            className="fw-bold mb-0"
                                            style={{
                                                color: "#dc3545"
                                            }}
                                        >
                                            {pendingCustomers.length}
                                        </h2>

                                    </div>


                                    <div
                                        className="d-flex align-items-center justify-content-center"
                                        style={{
                                            width: "52px",
                                            height: "52px",
                                            borderRadius: "12px",
                                            backgroundColor: "#fdeaea"
                                        }}
                                    >

                                        <i
                                            className="bi bi-person-x"
                                            style={{
                                                fontSize: "24px",
                                                color: "#dc3545"
                                            }}
                                        ></i>

                                    </div>

                                </div>

                            </div>

                        </div>

                    </div>


                    {/* DELETED */}

                    <div className="col-md-4">

                        <div
                            className="card border-0 shadow-sm h-100"
                            style={{
                                borderRadius: "14px"
                            }}
                        >

                            <div className="card-body p-4">

                                <div className="d-flex justify-content-between align-items-center">

                                    <div>

                                        <p className="text-muted mb-2">
                                            Deleted Customers
                                        </p>

                                        <h2
                                            className="fw-bold mb-0"
                                            style={{
                                                color: "#842029"
                                            }}
                                        >
                                            {deletedCustomers.length}
                                        </h2>

                                    </div>


                                    <div
                                        className="d-flex align-items-center justify-content-center"
                                        style={{
                                            width: "52px",
                                            height: "52px",
                                            borderRadius: "12px",
                                            backgroundColor: "#f8d7da"
                                        }}
                                    >

                                        <i
                                            className="bi bi-person-x"
                                            style={{
                                                fontSize: "24px",
                                                color: "#842029"
                                            }}
                                        ></i>

                                    </div>

                                </div>

                            </div>

                        </div>

                    </div>


                    {/* TOTAL */}

                    <div className="col-md-4">

                        <div
                            className="card border-0 shadow-sm h-100"
                            style={{
                                borderRadius: "14px"
                            }}
                        >

                            <div className="card-body p-4">

                                <div className="d-flex justify-content-between align-items-center">

                                    <div>

                                        <p className="text-muted mb-2">
                                            Total Records
                                        </p>

                                        <h2
                                            className="fw-bold mb-0"
                                            style={{
                                                color: "#1261a0"
                                            }}
                                        >
                                            {customers.length}
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
                                            className="bi bi-people"
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

                </div>


                {/* =================================================
                    TABLE
                ================================================= */}

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
                                    Customer Accounts
                                </h5>

                                <small className="text-muted">
                                    Customers who requested account deletion.
                                </small>

                            </div>


                            <span
                                className="badge px-3 py-2"
                                style={{
                                    backgroundColor: "#e8f1f8",
                                    color: "#1261a0"
                                }}
                            >
                                {customers.length} Customer
                                {customers.length !== 1 ? "s" : ""}
                            </span>

                        </div>


                        {/* =================================================
                            LOADING
                        ================================================= */}

                        {loading ? (

                            <div className="text-center py-5">

                                <div
                                    className="spinner-border"
                                    style={{
                                        color: "#1261a0"
                                    }}
                                ></div>

                                <p className="text-muted mt-3 mb-0">
                                    Loading customer requests...
                                </p>

                            </div>

                        ) : customers.length === 0 ? (

                            /* =================================================
                               EMPTY
                            ================================================= */

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
                                    No Customer Requests
                                </h5>


                                <p className="text-muted mb-0">
                                    There are no customer deletion requests.
                                </p>

                            </div>

                        ) : (

                            /* =================================================
                               TABLE
                            ================================================= */

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
                                                Customer
                                            </th>

                                            <th className="py-3">
                                                Email
                                            </th>

                                            <th className="py-3">
                                                Phone
                                            </th>

                                            <th className="py-3">
                                                Address
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

                                        {customers.map((customer, index) => (

                                            <tr key={customer.id}>


                                                {/* S.NO */}

                                                <td className="fw-semibold text-muted">
                                                    {index + 1}
                                                </td>


                                                {/* CUSTOMER */}

                                                <td>

                                                    <div className="d-flex align-items-center">

                                                        <div
                                                            className="d-flex align-items-center justify-content-center me-3"
                                                            style={{
                                                                width: "42px",
                                                                height: "42px",
                                                                borderRadius: "50%",
                                                                backgroundColor:
                                                                    customer.active
                                                                        ? "#e8f1f8"
                                                                        : "#f8d7da",
                                                                color:
                                                                    customer.active
                                                                        ? "#1261a0"
                                                                        : "#842029",
                                                                fontWeight: "bold"
                                                            }}
                                                        >

                                                            {customer.name
                                                                ?.charAt(0)
                                                                ?.toUpperCase() || "?"}

                                                        </div>


                                                        <div>

                                                            <div className="fw-semibold">
                                                                {customer.name || "N/A"}
                                                            </div>

                                                            <small className="text-muted">
                                                                Customer #{customer.id}
                                                            </small>

                                                        </div>

                                                    </div>

                                                </td>


                                                {/* EMAIL */}

                                                <td>
                                                    {customer.username || "N/A"}
                                                </td>


                                                {/* PHONE */}

                                                <td>
                                                    {customer.phoneNumber || "N/A"}
                                                </td>


                                                {/* ADDRESS */}

                                                <td>
                                                    {customer.address || "N/A"}
                                                </td>


                                                {/* STATUS */}

                                                <td>

                                                    {customer.active === false ? (

                                                        <span
                                                            className="badge px-3 py-2"
                                                            style={{
                                                                backgroundColor: "#f8d7da",
                                                                color: "#842029"
                                                            }}
                                                        >
                                                            <i className="bi bi-check-circle me-1"></i>
                                                            Customer Deleted
                                                        </span>

                                                    ) : customer.deletionRequested === true ? (

                                                        <span
                                                            className="badge px-3 py-2"
                                                            style={{
                                                                backgroundColor: "#fff3cd",
                                                                color: "#856404"
                                                            }}
                                                        >
                                                            <i className="bi bi-clock me-1"></i>
                                                            Deletion Requested
                                                        </span>

                                                    ) : (

                                                        <span
                                                            className="badge px-3 py-2"
                                                            style={{
                                                                backgroundColor: "#e8f5e9",
                                                                color: "#198754"
                                                            }}
                                                        >
                                                            Active
                                                        </span>

                                                    )}

                                                </td>


                                                {/* ACTION */}

                                                <td className="text-center">

                                                    {customer.active === false ? (

                                                        <span className="text-danger fw-semibold">

                                                            <i className="bi bi-check-circle me-1"></i>

                                                            Deleted

                                                        </span>

                                                    ) : customer.deletionRequested === true ? (

                                                        <button
                                                            className="btn btn-sm btn-danger px-3"
                                                            disabled={
                                                                deletingId === customer.id
                                                            }
                                                            onClick={() =>
                                                                handleDelete(customer.id)
                                                            }
                                                        >

                                                            {deletingId === customer.id ? (

                                                                <>
                                                                    <span
                                                                        className="spinner-border spinner-border-sm me-2"
                                                                    ></span>

                                                                    Deleting...
                                                                </>

                                                            ) : (

                                                                <>
                                                                    <i className="bi bi-trash me-1"></i>

                                                                    Delete Customer
                                                                </>

                                                            )}

                                                        </button>

                                                    ) : (

                                                        <span className="text-muted">
                                                            No Request
                                                        </span>

                                                    )}

                                                </td>

                                            </tr>

                                        ))}

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

export default AdminCustomerDeletion;
