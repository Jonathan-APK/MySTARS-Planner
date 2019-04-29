/**
	Represents a control class for the admin to manage courses.
	An admin can add/update courses.
	@author Lim Boon Leng
	@version 1.3
	@since 2017-04-12
*/

package Control;

import java.util.ArrayList;
import java.util.List;

import Objects.Course;
import Objects.IndexGroup;

public class CourseMgmtController {
	/**
	* Adds a new course to the existing list of courses
	* @param courseToAdd The course to add
	* @return DB operation boolean.
	*/
	public static boolean addCourse(Course courseToAdd) {
		boolean success = false;
		List<Course> courseList = new ArrayList<Course>();
		Course course = new Course();
		
		try {
			courseList = course.retrieveAllCourseObjects();
			courseList.add(courseToAdd);
			course.writeSerializedObject(courseList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}

	/**
	* Updates an existing course from existing list of courses
	* @param courseToUpdate The updated course
	* @return DB operation boolean.
	*/
	public static boolean updateCourse(Course courseToUpdate) {
		boolean success = false;
		List<Course> courseList = new ArrayList<Course>();
		Course course = new Course();

		try {
			courseList = course.retrieveAllCourseObjects();
			for (int i = 0; i < courseList.size(); i++) {
				if (courseList.get(i).getCourseID().equals(courseToUpdate.getCourseID())) {
					courseList.set(i, courseToUpdate);
				}
			}
			course.writeSerializedObject(courseList);
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}
	
	/**
	* Add a new index group to the existing list
	* @param grpToAdd The index group to be added
	* @return DB operation boolean.
	*/
	public static boolean addIndexGrp(IndexGroup grpToAdd) {
		boolean success = false;
		List<IndexGroup> indexGroupList = new ArrayList<IndexGroup>();
		IndexGroup indexGroup = new IndexGroup();

		try {
			indexGroupList = indexGroup.retrieveAllIndexGroupObjects();
			indexGroupList.add(grpToAdd);
			indexGroup.writeSerializedObject(indexGroupList);
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return success;
	}
}
