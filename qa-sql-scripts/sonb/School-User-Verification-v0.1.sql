-- MySQL script created by Nilutpal Dutta.
-- Script to validate user data after excel sheet import.
-- Script version: "v0.1-user", Date: 14-Feb-2017
-- ------------------------------------------------------
-- Enter UUID value in parameter "@Target_Uuid"
-- ------------------------------------------------------
--
SET @Target_Uuid = '29405618';
-- ------------------------------------------------------
SELECT DISTINCT
	concat_ws('', SCL_USERS.first_name,' ', SCL_USERS.middle_name, ' ', SCL_USERS.last_name) AS 'User Full Name',
	SCL_DETAILS.scl_name AS 'School Name',
	SCL_DETAILS.scl_code_ref AS 'School Code',
	SCL_DETAILS.registration_code AS 'Registration Code',
	concat_ws('', SCL_USERS.address_line1,' ', SCL_USERS.address_line2,' ', SCL_USERS.city_id,' ', SCL_USERS.state_id,' ', SCL_USERS.zip_code) AS 'User Address',
	SCL_USERS.login_id AS 'User Login ID',
	SCL_USERS.password AS 'User Password',
	SCL_USERS.email_id AS 'User Email',
	SCL_USERS.mobile_number AS 'User Mobile',
	SCL_USERS.invite_code AS 'User Invite Code',
	SCL_USERS.status AS 'User Status',
	SCL_GROUPS.group_name AS 'User Groups',
	SCL_GROUPS.description AS 'User School Role',
	SCL_DETAILS.class_name AS 'Class Group Name',
	SCL_DETAILS.section_name AS 'Section Group Name',
	SCL_DETAILS.subj_name AS 'Subject Group Name',
	SCL_GROUPS.base_profile_code AS 'Base Profile Code'
FROM
	( SELECT
		usrmast.uuid AS user_uid,
		usrmast.login_id,
		usrmast.password,
		usrmast.first_name,
		usrmast.middle_name,
		usrmast.last_name,
		usrmast.invite_code,
		usrmast.status,
		usrprof.email_id,
		usrprof.mobile_number,
		usrprof.address_line1,
		usrprof.address_line2,
		usrprof.city_id,
		usrprof.state_id,
		usrprof.zip_code,
		usrprof.user_type,
		usrprof.gender,
		usrprof.nickname
	FROM
		ums_api.user_master usrmast
		JOIN ums_api.user_profile usrprof ON usrprof.user_id = usrmast.id
	WHERE
		usrmast.uuid = @Target_Uuid
	) SCL_USERS
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
		group_api.group_users grpusr
		JOIN group_api.group_master grpmast ON grpmast.id = grpusr.group_id
		JOIN group_api.group_user_roles grpusrol ON grpusrol.group_user_id = grpusr.id
		LEFT JOIN group_api.school_roles grpsclrol ON grpusrol.school_role_code = grpsclrol.school_role_code
	) SCL_GROUPS
ON SCL_GROUPS.user_uid = SCL_USERS.user_uid
LEFT JOIN
	(SELECT
		slmast.school_code AS scl_code_ref,
		slmast.registration_code,
		slmast.name AS scl_name,
		slclss.class_name,
		slclsec.section_name,
		secsubj.name AS subj_name,
		slbrd.code,
		slacs.session_year
	FROM	
		school_api.school_master slmast
		JOIN school_api.school_class slclss ON slmast.id = slclss.school_id
        JOIN school_api.school_class_section slclsec ON slclss.id = slclsec.class_id
		JOIN school_api.section_subject secsubj ON slclsec.id = secsubj.section_id
		LEFT JOIN school_api.boards slbrd ON slmast.board_id = slbrd.id
		LEFT JOIN school_api.ayid slacs ON slmast.ayid = slacs.ayid
	) SCL_DETAILS
ON SCL_DETAILS.scl_code_ref = SCL_GROUPS.scl_code_ref
ORDER BY
	SCL_DETAILS.scl_code_ref,SCL_DETAILS.class_name,SCL_DETAILS.section_name,SCL_DETAILS.subj_name
LIMIT
	0,1000;