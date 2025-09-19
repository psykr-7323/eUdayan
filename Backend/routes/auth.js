const express = require("express");
const { register, login, getProfile } = require("../controllers/authController.js");
const authMiddleware = require("../middleware/authMiddleware.js");
const authorizeRoles = require("../middleware/roleMiddleware.js");

const router = express.Router();

// Signup
router.post("/signup", register);

// Login
router.post("/login", login);

// Profile route (any logged-in user)
router.get("/me", authMiddleware.protect, getProfile);

// Admin-only route
router.get("/admin", authMiddleware.protect, authorizeRoles("admin"), (req, res) => {
  res.json({ msg: "Welcome Admin 🚀", user: req.user });
});

module.exports = router;
