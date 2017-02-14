-- MySQL script created by Nilutpal Dutta.
-- Script to validate school data after excel sheet import.
-- Script version: "v0.1-school", Date: 14-Feb-2017
-- ------------------------------------------------------
-- Enter registration code as imported in parameter "@Target_Reg_Code"
-- ------------------------------------------------------
SET @Target_Reg_Code = 'Testing13022017';
-- ------------------------------------------------------
SELECT DISTINCT
	SCL_DETAILS.scl_code_ref AS 'School Code',
	SCL_DETAILS.registration_code AS 'Registration Code',
	SCL_DETAILS.name AS 'School Name',
	SCL_DETAILS.address AS 'School Address',
	SCL_DETAILS.website AS 'School URL',
	SCL_DETAILS.email AS 'School Email',
	SCL_DETAILS.contacts AS 'School Phone',
	SCL_DETAILS.ayid_start_date AS 'Academic Start Date',
	SCL_DETAILS.ayid_end_date AS 'Academic End Date',
	SCL_DETAILS.city_id AS 'School City',
	SCL_DETAILS.state_id AS 'School State',
	SCL_DETAILS.code AS 'School Board',
	SCL_DETAILS.session_year AS 'School Session',
	SCL_GROUPS.group_name AS 'School Group Name',
	concat_ws('', SCL_USERS.first_name,' ', SCL_USERS.middle_name, ' ', SCL_USERS.last_name) AS 'School User Name',
	SCL_GROUPS.description AS 'School User Role'
	
FROM
	(SELECT
		slmast.school_code AS scl_code_ref,
		slmast.registration_code,
		slmast.name,
		slmast.address,
		slmast.website,
		slmast.email,
		slmast.contacts,
		slmast.created,
		slmast.ayid_start_date,
		slmast.ayid_end_date,
		slmast.city_id,
		slmast.state_id,
		slbrd.code,
		slacs.session_year
	FROM	
		school_api.school_master slmast
		LEFT JOIN school_api.boards slbrd ON slmast.board_id = slbrd.id
		LEFT JOIN school_api.ayid slacs ON slmast.ayid = slacs.ayid
	
	WHERE
		slmast.registration_code = @Target_Reg_Code 
	) SCL_DETAILS
	
LEFT JOIN
	( SELECT
		grpmast.school_code AS scl_code_ref,
		grpmast.group_name,
		grpmast.group_code,
		grpusr.uuid AS user_uid,
		grpusrol.school_role_code,
		grpusrol.base_profile_code,
		grpusrol.profile_name,
		grpsclrol.description
	FROM
		group_api.group_master grpmast
		JOIN group_api.group_users grpusr ON grpusr.group_id = grpmast.id
		JOIN group_api.group_user_roles grpusrol ON grpusrol.group_user_id = grpusr.id
		LEFT JOIN group_api.school_roles grpsclrol ON grpusrol.school_role_code = grpsclrol.school_role_code
	) SCL_GROUPS
ON SCL_GROUPS.scl_code_ref = SCL_DETAILS.scl_code_ref

LEFT JOIN
	( SELECT
		usrmast.uuid AS user_uid,
		usrmast.login_id,
		usrmast.first_name,
		usrmast.middle_name,
		usrmast.last_name,
		usrprof.email_id,
		usrprof.mobile_number,
		usrprof.address_line1,
		usrprof.address_line2
	FROM
		ums_api.user_master usrmast
		JOIN ums_api.user_profile usrprof ON usrprof.user_id = usrmast.id
	) SCL_USERS
ON SCL_USERS.user_uid = SCL_GROUPS.user_uid
LIMIT
	0,10;