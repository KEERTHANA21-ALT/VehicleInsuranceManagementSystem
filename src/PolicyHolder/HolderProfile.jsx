
import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router";
import HolderNavbar from "./HolderNavbar";

function HolderProfile() {

    const navigate = useNavigate();

    const [profile, setProfile] = useState({});
    const [deletionRequested, setDeletionRequested] = useState(false);

    const token = localStorage.getItem("token");

    const config = {
        headers: {
            Authorization: "Bearer " + token,
        },
    };

    useEffect(() => {

        const fetchProfile = async () => {

            try {

                const response = await axios.get(
                    "http://localhost:8080/api/policyHolder/profile",
                    config
                );

                setProfile(response.data);

            } catch (err) {

                console.log(
                    "Profile error:",
                    err.response?.data || err.message
                );

            }

        };

        fetchProfile();

    }, []);

    const onLogout = () => {

        localStorage.clear();
        navigate("/");

    };


    // =========================================================
    // REQUEST ACCOUNT DELETION
    // =========================================================

    const deleteAccount = async () => {

        const confirmDelete = window.confirm(
            "Are you sure you want to request account deletion?"
        );

        if (!confirmDelete) {
            return;
        }

        try {

            await axios.post(
                "http://localhost:8080/api/policyHolder/request-deletion",
                {},
                config
            );

            setDeletionRequested(true);

            alert(
                "Account deletion request has been sent to the admin. Your account will be deleted only after admin approval."
            );

        } catch (err) {

            console.log(
                "Deletion request error:",
                err.response?.data || err.message
            );

            alert(
                err.response?.data ||
                "Failed to submit account deletion request."
            );

        }

    };


    return (

        <div className="bg-light min-vh-100">

            <HolderNavbar />

            <div className="container py-5">

                <div className="row justify-content-center">

                    <div className="col-md-7">

                        <div className="card shadow border-0 rounded-4">

                            {/* HEADER */}

                            <div
                                className="text-center text-white py-4 rounded-top"
                                style={{
                                    backgroundColor: "#4A90E2"
                                }}
                            >

                                <div
                                    className="bg-white text-primary rounded-circle mx-auto mb-3 d-flex justify-content-center align-items-center"
                                    style={{
                                        width: "80px",
                                        height: "80px",
                                        fontSize: "35px"
                                    }}
                                >
                                    {profile.name?.charAt(0)?.toUpperCase()}
                                </div>

                                <h3 className="mb-1">
                                    {profile.name}
                                </h3>

                            </div>


                            {/* DETAILS */}

                            <div className="card-body p-4">

                                <h5 className="mb-4 text-primary">
                                    Personal Information
                                </h5>


                                <div className="row mb-3">

                                    <div className="col-5 fw-bold text-secondary">
                                        Email:
                                    </div>

                                    <div className="col-7">
                                        {profile.username || "Not Provided"}
                                    </div>

                                </div>


                                <div className="row mb-3">

                                    <div className="col-5 fw-bold text-secondary">
                                        Date of Birth:
                                    </div>

                                    <div className="col-7">
                                        {profile.dob || "Not Provided"}
                                    </div>

                                </div>


                                <div className="row mb-3">

                                    <div className="col-5 fw-bold text-secondary">
                                        Phone Number:
                                    </div>

                                    <div className="col-7">
                                        {profile.phoneNumber || "Not Provided"}
                                    </div>

                                </div>


                                <div className="row mb-3">

                                    <div className="col-5 fw-bold text-secondary">
                                        Address:
                                    </div>

                                    <div className="col-7">
                                        {profile.address || "Not Provided"}
                                    </div>

                                </div>


                                <hr />


                                {/* ACCOUNT ACTIONS */}

                                <div className="d-flex justify-content-center gap-3 mt-4">

                                    <button
                                        className="btn btn-outline-danger px-4"
                                        onClick={deleteAccount}
                                        disabled={deletionRequested}
                                    >

                                        

                                        {deletionRequested
                                            ? "Deletion Requested"
                                            : "Request Account Deletion"}

                                    </button>


                                    <button
                                        className="btn btn-primary px-4"
                                        onClick={onLogout}
                                    >

                                        

                                        Logout

                                    </button>

                                </div>


                                {deletionRequested && (

                                    <div className="alert alert-warning mt-4 mb-0">

                                        <i className="bi bi-clock me-2"></i>

                                        Your account deletion request is pending
                                        admin approval.

                                    </div>

                                )}

                            </div>

                        </div>

                    </div>

                </div>

            </div>

        </div>

    );

}

export default HolderProfile;

