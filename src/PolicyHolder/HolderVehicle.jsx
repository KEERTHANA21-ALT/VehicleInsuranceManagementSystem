import { useEffect, useState } from "react"
import axios from "axios"
import { useNavigate } from "react-router"
import HolderNavbar from "./HolderNavbar"

function HolderVehicle() {
    const navigate = useNavigate()

    const [vehicles, setVehicles] = useState([])

    const [vehicle, setVehicle] = useState({
        vehicleNumber: "",
        vehicleModel: "",
        vehicleType: "",
        year: ""
    })

    const [img, setImg] = useState("")
    const [msg, setMsg] = useState("")

    const token = localStorage.getItem("token")

    const config = {
        headers: {
            Authorization: "Bearer " + token
        }
    }

    // GET MY VEHICLES
    useEffect(() => {
        const getVehicles = async () => {
            try {
                const response = await axios.get(
                    "http://localhost:8080/api/vehicle/get-myVehicles",
                    config
                )

                setVehicles(response.data)
            } catch (error) {
                console.log(error)
            }
        }

        getVehicles()
    }, [])

    // VEHICLE NUMBER
    const handleVehicleNumber = (event) => {
        setVehicle({
            ...vehicle,
            vehicleNumber: event.target.value
        })
    }

    // VEHICLE MODEL
    const handleVehicleModel = (event) => {
        setVehicle({
            ...vehicle,
            vehicleModel: event.target.value
        })
    }

    // VEHICLE TYPE
    const handleVehicleType = (event) => {
        setVehicle({
            ...vehicle,
            vehicleType: event.target.value
        })
    }

    // YEAR
    const handleYear = (event) => {
        setVehicle({
            ...vehicle,
            year: event.target.value
        })
    }

    // ADD VEHICLE
    const addVehicle = async (event) => {
        event.preventDefault()

        try {
            // STEP 1: ADD VEHICLE
            const response = await axios.post(
                "http://localhost:8080/api/vehicle/add",
                vehicle,
                config
            )

            const vehicleId = response.data.id

            console.log("Vehicle ID:", vehicleId)

            // STEP 2: UPLOAD IMAGE
            if (img) {
                const formData = new FormData()

                formData.append("vImage", img)

                const imageResponse = await axios.post(
                    "http://localhost:8080/api/vehicle/image/upload/" + vehicleId,
                    formData,
                    config
                )

                console.log("Image uploaded:", imageResponse.data)

                setMsg(imageResponse.data.message)
            }

            // STEP 3: SUCCESS
            alert("Vehicle Added Successfully")

            // STEP 4: CLEAR FORM
            setVehicle({
                vehicleNumber: "",
                vehicleModel: "",
                vehicleType: "",
                year: ""
            })

            setImg("")

            // STEP 5: GET VEHICLES AGAIN
            const result = await axios.get(
                "http://localhost:8080/api/vehicle/get-myVehicles",
                config
            )

            setVehicles(result.data)
        } catch (error) {
            console.log(error)

            setMsg(error.response?.data?.message)

            alert("Vehicle Adding Failed")
        }
    }

    return (
        <div className="bg-light min-vh-100">
            <HolderNavbar />

            <div className="container py-5">
                {/* HEADER */}
                <div className="mb-4">
                    <h2 className="fw-bold text-primary mb-1">
                        My Vehicles
                    </h2>

                    <p className="text-muted">
                        Manage your registered vehicles and create insurance proposals.
                    </p>
                </div>

                {/* ADD VEHICLE */}
                <div className="card border-0 shadow-lg rounded-4 mb-5">
                    <div className="card-body p-4">
                        <h4 className="fw-bold mb-4 text-primary">
                            Add New Vehicle
                        </h4>

                        <form onSubmit={addVehicle}>
                            <div className="row">
                                {/* VEHICLE NUMBER */}
                                <div className="col-md-6 mb-3">
                                    <label className="form-label fw-semibold">
                                        Vehicle Number
                                    </label>

                                    <input
                                        className="form-control"
                                        value={vehicle.vehicleNumber}
                                        onChange={handleVehicleNumber}
                                        placeholder="TN01AB1234"
                                        required
                                    />
                                </div>

                                {/* VEHICLE MODEL */}
                                <div className="col-md-6 mb-3">
                                    <label className="form-label fw-semibold">
                                        Vehicle Model
                                    </label>

                                    <input
                                        className="form-control"
                                        value={vehicle.vehicleModel}
                                        onChange={handleVehicleModel}
                                        placeholder="Swift, Activa..."
                                        required
                                    />
                                </div>

                                {/* VEHICLE TYPE */}
                                <div className="col-md-6 mb-3">
                                    <label className="form-label fw-semibold">
                                        Vehicle Type
                                    </label>

                                    <select
                                        className="form-select"
                                        value={vehicle.vehicleType}
                                        onChange={handleVehicleType}
                                        required
                                    >
                                        <option value="">
                                            Select Vehicle Type
                                        </option>

                                        <option value="CAR">
                                            Car
                                        </option>

                                        <option value="TWO_WHEELER">
                                            Two Wheeler
                                        </option>
                                    </select>
                                </div>

                                {/* YEAR */}
                                <div className="col-md-6 mb-3">
                                    <label className="form-label fw-semibold">
                                        Manufacturing Year
                                    </label>

                                    <input
                                        type="number"
                                        className="form-control"
                                        value={vehicle.year}
                                        onChange={handleYear}
                                        min="1980"
                                        max="2050"
                                        required
                                    />
                                </div>

                                {/* IMAGE */}
                                <div className="col-md-6 mb-4">
                                    <label className="form-label fw-semibold">
                                        Vehicle Image
                                    </label>

                                    <input
                                        type="file"
                                        className="form-control"
                                        accept=".png,.jpg,.jpeg"
                                        onChange={(event) =>
                                            setImg(event.target.files[0])
                                        }
                                        required
                                    />
                                </div>
                            </div>

                            {msg && (
                                <div className="alert alert-success">
                                    {msg}
                                </div>
                            )}

                            <button
                                type="submit"
                                className="btn btn-primary px-4"
                            >
                                Add Vehicle
                            </button>
                        </form>
                    </div>
                </div>

                {/* REGISTERED VEHICLES */}
                <div className="d-flex justify-content-between align-items-center mb-3">
                    <h3 className="fw-bold">
                        Registered Vehicles
                    </h3>

                    <span className="badge bg-primary fs-6">
                        {vehicles.length} Vehicle
                        {vehicles.length !== 1 ? "s" : ""}
                    </span>
                </div>

                {/* VEHICLE LIST */}
                <div className="row">
                    {vehicles.length === 0 ? (
                        <div className="col-12">
                            <div className="card border-0 shadow rounded-4">
                                <div className="card-body text-center py-5">
                                    <h3 className="mb-3">
                                        🚗
                                    </h3>

                                    <h4 className="fw-bold">
                                        No Vehicles Registered
                                    </h4>

                                    <p className="text-muted mb-0">
                                        Add your first vehicle to create an insurance proposal.
                                    </p>
                                </div>
                            </div>
                        </div>
                    ) : (
                        vehicles.map((v, index) => (
                            <div
                                className="col-lg-4 col-md-6 mb-4"
                                key={index}
                            >
                                <div className="card border-0 shadow-lg rounded-4 h-100">
                                    {/* IMAGE */}
                                    {v.imageUrl && (
                                        <img
                                            src={
                                                "http://localhost:5173" + v.imageUrl
                                            }
                                            className="card-img-top rounded-top-4"
                                            style={{
                                                height: "220px",
                                                objectFit: "cover"
                                            }}
                                            alt={v.vehicleModel}
                                        />
                                    )}

                                    <div className="card-body d-flex flex-column">
                                        {/* MODEL */}
                                        <h4 className="fw-bold text-primary mb-3">
                                            {v.vehicleModel}
                                        </h4>

                                        {/* VEHICLE NUMBER */}
                                        <div className="mb-2">
                                            <strong>
                                                🚘 Vehicle Number
                                            </strong>

                                            <div className="text-muted">
                                                {v.vehicleNumber}
                                            </div>
                                        </div>

                                        {/* VEHICLE TYPE */}
                                        <div className="mb-2">
                                            <strong>
                                                🚗 Vehicle Type
                                            </strong>

                                            <div className="text-muted">
                                                {v.vehicleType === "CAR"
                                                    ? "Car"
                                                    : "Two Wheeler"}
                                            </div>
                                        </div>

                                        {/* YEAR */}
                                        <div className="mb-4">
                                            <strong>
                                                📅 Manufacturing Year
                                            </strong>

                                            <div className="text-muted">
                                                {v.vehicleYear}
                                            </div>
                                        </div>

                                        {/* CREATE PROPOSAL */}
                                        <div className="mt-auto">
                                            <button
                                                className="btn btn-success w-100 rounded-pill"
                                                onClick={() =>
                                                    navigate(
                                                        "/holder/create-proposal/" +
                                                        v.id
                                                    )
                                                }
                                            >
                                                Create Proposal
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        ))
                    )}
                </div>
            </div>
        </div>
    )
}

export default HolderVehicle