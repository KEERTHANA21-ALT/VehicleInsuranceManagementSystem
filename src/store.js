
import { configureStore } from "@reduxjs/toolkit";
import SurveyReducer from "./Reducer/SurveyReducer";

export default configureStore({
    reducer:{
        surveySlice: SurveyReducer
    }
})

