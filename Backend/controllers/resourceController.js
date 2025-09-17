const Resource = require("../models/resource");
const { successResponse, errorResponse } = require("../utils/response");

// @desc    Add a new resource
// @route   POST /api/resources
// @access  Private/Admin
const addResource = async (req, res) => {
  try {
    const { title, type, url, category } = req.body;
    const resource = await Resource.create({ title, type, url, category });
    return successResponse(res, "Resource added successfully", resource, 201);
  } catch (error) {
    console.error(error);
    return errorResponse(res, "Failed to add resource", 400);
  }
};

// @desc    Get all resources
// @route   GET /api/resources
// @access  Public
const getResources = async (req, res) => {
  try {
    const resources = await Resource.find({});
    return successResponse(res, "Resources fetched successfully", resources);
  } catch (error) {
    console.error(error);
    return errorResponse(res, "Failed to fetch resources");
  }
};

// @desc    Update a resource
// @route   PUT /api/resources/:id
// @access  Private/Admin
const updateResource = async (req, res) => {
  try {
    const { id } = req.params;
    const { title, type, url, category } = req.body;

    const resource = await Resource.findById(id);

    if (!resource) {
      return errorResponse(res, "Resource not found", 404);
    }

    resource.title = title || resource.title;
    resource.type = type || resource.type;
    resource.url = url || resource.url;
    resource.category = category || resource.category;

    const updatedResource = await resource.save();
    return successResponse(res, "Resource updated successfully", updatedResource);
  } catch (error) {
    console.error(error);
    return errorResponse(res, "Failed to update resource", 400);
  }
};

// @desc    Delete a resource
// @route   DELETE /api/resources/:id
// @access  Private/Admin
const deleteResource = async (req, res) => {
  try {
    const { id } = req.params;
    const resource = await Resource.findById(id);

    if (!resource) {
      return errorResponse(res, "Resource not found", 404);
    }

    await resource.deleteOne();
    return successResponse(res, "Resource removed successfully");
  } catch (error) {
    console.error(error);
    return errorResponse(res, "Failed to delete resource");
  }
};

// Add feedback (Student only)
const addFeedback = async (req, res) => {
  try {
    const { id } = req.params; // resource id
    const { comment, rating } = req.body;

    const resource = await Resource.findById(id);
    if (!resource) {
      return errorResponse(res, "Resource not found", 404);
    }

    const feedback = {
      student: req.user.id, // logged-in student
      comment,
      rating,
    };

    resource.feedback.push(feedback);
    await resource.save();

    const addedFeedback = resource.feedback[resource.feedback.length - 1];
    return successResponse(res, "Feedback added", addedFeedback, 201);
  } catch (error) {
    console.error(error);
    return errorResponse(res, "Failed to add feedback");
  }
};

// View feedback of a resource (Public)
const getFeedback = async (req, res) => {
  try {
    const { id } = req.params;
    const resource = await Resource.findById(id).populate("feedback.student", "schoolInfo");
    // NOTE: only showing school info, not student name for anonymity

    if (!resource) {
      return errorResponse(res, "Resource not found", 404);
    }

    return successResponse(res, "Feedback fetched successfully", resource.feedback);
  } catch (error) {
    console.error(error);
    return errorResponse(res, "Failed to fetch feedback");
  }
};

module.exports = {
  addResource,
  getResources,
  updateResource,
  deleteResource,
  addFeedback,
  getFeedback,
};
