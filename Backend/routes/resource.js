const express = require("express");
const {
  addResource,
  getResources,
  updateResource,
  deleteResource,
  addFeedback,
  getFeedback,
} = require("../controllers/resourceController");
const { protect, adminOnly } = require("../middleware/authMiddleware");

const router = express.Router();

// Public
router.get("/", getResources);
router.get("/:id/feedback", getFeedback); // view feedback of a resource

// Admin
router.post("/", protect, adminOnly, addResource);
router.put("/:id", protect, adminOnly, updateResource);
router.delete("/:id", protect, adminOnly, deleteResource);

// Student (must be logged in)
router.post("/:id/feedback", protect, addFeedback);

module.exports = router;
