
import axios from "axios";


// =========================================================
// GET ASSIGNED SURVEYS
// =========================================================

export const getAssignedSurveys = () => async (dispatch) => {

    try {

        const token = localStorage.getItem("token");

        const response = await axios.get(
            "http://localhost:8080/api/claim/surveyor/pending",
            {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            }
        );


        dispatch({
            type: "GET_ASSIGNED_SURVEYS",
            payload: response.data
        });

    } catch (error) {

        console.log(
            "Get assigned surveys error:",
            error.response?.data || error.message
        );

        throw error;
    }
};


// =========================================================
// UPDATE / COMPLETE SURVEY
// =========================================================

export const updateSurvey =
    (id, surveyData) =>
    async (dispatch) => {

        try {

            const token =
                localStorage.getItem("token");


            console.log(
                "Updating survey:",
                id,
                surveyData
            );


            const response = await axios.put(

                `http://localhost:8080/api/claim/surveyor/review/${id}`,

                surveyData,

                {
                    headers: {
                        Authorization:
                            `Bearer ${token}`,

                        "Content-Type":
                            "application/json"
                    }
                }

            );


            return response.data;

        } catch (error) {

            console.log(
                "Update survey error:",
                error.response?.data ||
                error.message
            );

            throw error;
        }
    };

