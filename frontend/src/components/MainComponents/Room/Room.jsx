import * as React from "react";
import RoomSideBar from "./RoomSideBar";
import RoomList from "./RoomList";
import { useState, useEffect } from "react";
import Box from "@mui/material/Box";
import Tab from "@mui/material/Tab";
import TabContext from "@mui/lab/TabContext";
import TabList from "@mui/lab/TabList";
import TabPanel from "@mui/lab/TabPanel";
import RoomType from "./RoomType";
import userApi from "../../../api/userApi";
import PropTypes from "prop-types";

function Room({ session }) {
  const [typeSelected, setTypeSelected] = useState(null);
  const [typeList, setTypeList] = useState([]);
  const [statusSelected, setStatusSelected] = useState(null);
  const [tabValue, setTabValue] = useState("1");

  const handleTypeSelected = (type) => {
    setTypeSelected(type);
  };

  const handleStatusSelected = (status) => {
    setStatusSelected(status);
  };

  const handleChangeTab = (event, newValue) => {
    setTabValue(newValue);
  };

  // Fetch typeList from API on mount
  useEffect(() => {
    const fetchTypes = async () => {
      try {
        const response = await userApi.getType();
        setTypeList(response);
      } catch (error) {
        console.error("Failed to fetch types", error);
      }
    };
    fetchTypes();
  }, []);

  // Callback to add or update a room type
  const onSaveType = async (newData, isEdit = false, typeId = null) => {
    try {
      if (isEdit) {
        await userApi.updateType(typeId, newData);
      } else {
        await userApi.addType(newData);
      }
      // Refresh typeList from server after save
      const response = await userApi.getType();
      setTypeList(response);
    } catch (error) {
      console.error("Error saving type:", error);
    }
  };

  // Callback to delete a room type
  const onDeleteType = async (typeId) => {
    try {
      await userApi.deleteType(typeId);
      // Refresh typeList from server after delete
      const response = await userApi.getType();
      setTypeList(response);
    } catch (error) {
      console.error("Error deleting type:", error);
    }
  };

  return (
    <Box
      sx={{ width: "100%", typography: "body1", bgcolor: "background.paper" }}
    >
      <TabContext value={tabValue}>
        <Box sx={{ borderBottom: 1, borderColor: "divider" }}>
          <TabList
            onChange={handleChangeTab}
            centered
            sx={{
              "& .MuiTabs-indicator": {
                backgroundColor: "#009394",
              },
              "& .custom-tab-label": {
                color: "#132046",
              },
            }}
          >
            <Tab label="Room Type" value="1" />
            <Tab label="Room List" value="2" />
          </TabList>
        </Box>
        <TabPanel value="1" sx={{ padding: 0 }}>
          <RoomType
            typeList={typeList}
            onSaveType={onSaveType}
            onDeleteType={onDeleteType}
          />
        </TabPanel>
        <TabPanel value="2" sx={{ display: "flex", padding: 0 }}>
          <RoomSideBar
            onTypeSelected={handleTypeSelected}
            onStatusSelected={handleStatusSelected}
            typeList={typeList}
          />
          <RoomList
            selectedType={typeSelected}
            selectedStatus={statusSelected}
            typeList={typeList}
            session={session}
          />
        </TabPanel>
      </TabContext>
    </Box>
  );
}

Room.propTypes = {
  session: PropTypes.object,
};

export default Room;