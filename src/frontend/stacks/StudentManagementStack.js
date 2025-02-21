import React from "react";
import { createStackNavigator } from "@react-navigation/stack";
import StudentManagementScreen from "../pages/StudentManagementPage";
import StudentGradeScreen from "../pages/StudentGradePage";

const Stack = createStackNavigator();

const StudentManagementStack = () => {
  return (
    <Stack.Navigator screenOptions={{ headerShown: false }}>
      <Stack.Screen name="StudentManagement" component={StudentManagementScreen} />
      <Stack.Screen name="studentGrade" component={StudentGradeScreen} />
    </Stack.Navigator>
  );
};

export default StudentManagementStack;
