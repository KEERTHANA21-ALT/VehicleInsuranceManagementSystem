import HomeDashboard from "./components/HomeDashboard"
import { Route, Routes } from "react-router";
import HolderDashboard from "./PolicyHolder/HolderDashboard";
import EmployeeNavbar from "./Employee/EmployeeNavbar";
import EmployeeDashboard from "./Employee/EmployeeDashboard";
import AdminDashboard from "./Admin/AdminDashboard";
import Login from "./auth/Login";
import PageNotFound from "./PageNotFound";
import EmployeeManagement from "./Admin/EmployeeManagement";
import Signup from "./auth/Signup";
import HolderProfile from "./PolicyHolder/HolderProfile";
import InsurancePlanManagement from "./Admin/InsurancePlanManagement";
import ProposalManagement from "./Admin/AdminAssignment";
import EmployeeProposalManagement from "./Employee/EmployeeProposalManagement";
import EmployeePolicyManagement from "./Employee/EmployeePolicyManagement";
import EmployeePolicy from "./Employee/EmployeePolicy";
import HolderVehicle from "./PolicyHolder/HolderVehicle";
import HolderCreateProposal from "./PolicyHolder/HolderCreateProposal";
import HolderProposals from "./PolicyHolder/HolderProposals";
import HolderPolicies from "./PolicyHolder/HolderPolicies";
import HolderPayment from "./PolicyHolder/HolderPayment";
import HolderPolicyDetails from "./PolicyHolder/HolderPolicyDetails";
import HolderClaim from "./PolicyHolder/HolderClaim";
import EmployeeAssignedSurveys from "./Employee/EmployeeAssignedSurveys";
import EmployeePendingSurveys from "./Employee/EmployeePendingSurveys";
import EmployeeCompletedSurveys from "./Employee/EmployeeCompletedSurveys";
import EmployeeClaims from "./Employee/EmployeeClaims";
import EmployeeClaimPayments from "./Employee/EmployeeClaimPayments";
import AdminCustomerDeletion from "./Admin/AdminCustomerDeletion";
// import EmployeeClaimManagement from "./Employee/EmployeeClaimManagement";
// import EmployeeCompletedClaims from "./Employee/EmployeeCompletedClaims";
function App() {

  return (
    <Routes>
      <Route path="/" element={<HomeDashboard />} />
      <Route path="/login" element={<Login />} />
      <Route path="/sign-up" element={<Signup />} />
      <Route path="/holder" element={<HolderDashboard />} />
      <Route path="/holder/profile" element={<HolderProfile />} />
      <Route path="/holder/vehicles" element={<HolderVehicle />} />
      <Route path="/holder/create-proposal" element={<HolderCreateProposal/>} />
      <Route path="/holder/create-proposal/:id" element={<HolderCreateProposal />} />
      <Route path="/holder/proposals" element={<HolderProposals />}/>
      <Route path="/holder/payment/:proposalId" element={<HolderPayment />}/>
      <Route path="/holder/policies" element={<HolderPolicies />}/>
      <Route path="/holder/policies/:id" element={<HolderPolicyDetails />}/>
      <Route path="/holder/claims" element={<HolderClaim/>} />
      <Route path="/holder/claims" element={<HolderClaim />} />
      <Route path="/holder/claims/:policyId"element={<HolderClaim />}/>
      <Route path="/employee" element={<EmployeeDashboard />} />
      <Route path="/employee/proposals" element={<EmployeeProposalManagement />} />
      <Route path="/employee/policy/:proposalId" element={<EmployeePolicyManagement />} />
      <Route path="/employee/policy" element={<EmployeePolicy />} />
      <Route path="/employee/surveys" element={<EmployeeAssignedSurveys/>}/>
      <Route path="/employee/pending-surveys" element={<EmployeePendingSurveys/>}/>
      <Route path="/employee/completed-surveys" element={<EmployeeCompletedSurveys/>}/>
      <Route path="/employee/claims" element={<EmployeeClaims/>}/>
      <Route path="/employee/claim-payments" element={<EmployeeClaimPayments/>}/>
      
      <Route path="/admin" element={<AdminDashboard />} />
      <Route path="/admin/employees" element={<EmployeeManagement />} />
      <Route path="/admin/insurance-plan" element={<InsurancePlanManagement />} />
      <Route path="/admin/proposals" element={<ProposalManagement />} />
      <Route path="/admin/customers" element={<AdminCustomerDeletion />} />
      <Route path="*" element={<PageNotFound />} />


    </Routes>
  )
}

export default App
