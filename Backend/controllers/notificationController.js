const Notification = require("../models/notification");
const { successResponse, errorResponse } = require("../utils/response.js");

// Create notification
exports.createNotification = async (req, res) => {
  try {
    const { userId, message, type } = req.body;
    const notification = await Notification.create({ userId, message, type });
    return successResponse(res, "Notification created", notification, 201);
  } catch (err) {
    console.error(err);
    return errorResponse(res, "Error creating notification");
  }
};

// Get user’s notifications
exports.getNotifications = async (req, res) => {
  try {
    const notifications = await Notification.find({ userId: req.user.id }).sort({ createdAt: -1 });
    return successResponse(res, "Notifications fetched", notifications);
  } catch (err) {
    console.error(err);
    return errorResponse(res, "Error fetching notifications");
  }
};

// Mark notification as read
exports.markAsRead = async (req, res) => {
  try {
    const { id } = req.params;
    const notification = await Notification.findByIdAndUpdate(id, { isRead: true }, { new: true });
    if (!notification) {
      return errorResponse(res, "Notification not found", 404);
    }
    return successResponse(res, "Notification marked as read", notification);
  } catch (err) {
    console.error(err);
    return errorResponse(res, "Error updating notification");
  }
};
