-- MySQL script created by Nilutpal Dutta.
-- Script to validate school groups data after excel sheet import.
-- Script version: "v0.1-groups", Date: 14-Feb-2017
-- ------------------------------------------------------
-- Enter registration code as imported in parameter "@Target_Reg_Code"
-- ------------------------------------------------------
--
SET @Target_Reg_Code = 'Testing13022017';
-- ------------------------------------------------------
SELECT DISTINCT
	SCL_DETAILS.scl_code_ref AS 'School Code',
	SCL_DETAILS.registration_code AS 'Registration Code',
	SCL_DETAILS.scl_name AS 'School Name',
	SCL_DETAILS.class_name AS 'School Classes',
	SCL_DETAILS.section_name AS 'Class Sections',
	SCL_DETAILS.subj_name AS 'Class Subjects'	
FROM
	(SELECT
		slmast.school_code AS scl_code_ref,
		slmast.registration_code,
		slmast.name AS scl_name,
		slclss.class_name,
		slclsec.section_name,
		secsubj.name AS subj_name
	FROM	
		school_api.school_master slmast
		JOIN school_api.school_class slclss ON slmast.id = slclss.school_id
        JOIN school_api.school_class_section slclsec ON slclss.id = slclsec.class_id
		JOIN school_api.section_subject secsubj ON slclsec.id = secsubj.section_id
	WHERE
		slmast.registration_code = @Target_Reg_Code
	ORDER BY
		slclss.class_name,slclsec.section_name,secsubj.name
	) SCL_DETAILS
LIMIT
	0,1000;