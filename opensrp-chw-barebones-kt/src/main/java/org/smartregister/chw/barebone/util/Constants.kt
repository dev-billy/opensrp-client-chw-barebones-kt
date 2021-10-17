package org.smartregister.chw.barebone.util

/**
 * Contains constants used through out the application
 */
object Constants {
    const val EN ="en"
    const val SW ="sw"
    const val REQUEST_CODE_GET_JSON = 2244
    const val ENCOUNTER_TYPE = "encounter_type"
    const val STEP_ONE = "step1"
    const val STEP_TWO = "step2"

    object Configuration {
        const val TB_REGISTER = "tb_register"
    }

    object TbMemberObject {
        const val MEMBER_OBJECT = "memberObject"
        const val COMMON_PERSON_OBJECT = "commonPersonObjectClient"
    }

    object JsonFormExtra {
        const val JSON = "json"
        const val ENCOUNTER_TYPE = "encounter_type"
    }

    object EventType {
        const val REGISTRATION = "TB Registration"
        const val TB_OUTCOME = "TB Outcome"
        const val TB_COMMUNITY_FOLLOWUP = "TB Community Followup"
        const val TB_COMMUNITY_FOLLOWUP_FEEDBACK = "TB Community Followup Feedback"
        const val FOLLOW_UP_VISIT = "TB Followup"
        const val TB_CASE_CLOSURE = "TB Case Closure"
        const val REFERRAL_FOLLOW_UP_VISIT = "Followup Visit"
    }

    object TbStatus {
        const val POSITIVE = "positive"
        const val NEGATIVE = "negative"
        const val UNKNOWN = "unknown"
    }

    object Tables {
        const val TB = "ec_tb_register"
        const val TB_FOLLOWUP = "ec_tb_follow_up_visit"
        const val TB_COMMUNITY_FOLLOWUP = "ec_tb_community_followup"
        const val TB_COMMUNITY_FEEDBACK = "ec_tb_community_feedback"
        const val TB_OUTCOME = "ec_tb_outcome"
        const val FAMILY_MEMBER = "ec_family_member"
    }

    object ActivityPayload {
        const val BASE_ENTITY_ID = "BASE_ENTITY_ID"
        const val MEMBER_OBJECT = "MEMBER_OBJECT"
        const val ACTION = "ACTION"
        const val TB_REGISTRATION_FORM_NAME = "TB_REGISTRATION_FORM_NAME"
        const val USE_DEFAULT_NEAT_FORM_LAYOUT = "use_default_neat_form_layout"
        const val JSON_FORM = "JSON_FORM"
        const val TB_FOLLOWUP_FORM_NAME = "TB_FOLLOWUP_FORM_NAME"
    }

    object ActivityPayloadType {
        const val REGISTRATION = "REGISTRATION"
        const val FOLLOW_UP_VISIT = "FOLLOW_UP_VISIT"
    }

    object FamilyMemberEntityType {
        const val EC_INDEPENDENT_CLIENT = "ec_independent_client"
    }
}

object DBConstants {
    object Key {
        const val ID = "id"
        const val FIRST_NAME = "first_name"
        const val MIDDLE_NAME = "middle_name"
        const val LAST_NAME = "last_name"
        const val BASE_ENTITY_ID = "base_entity_id"
        const val ENTITY_ID = "entity_id"
        const val FAMILY_BASE_ENTITY_ID = "family_base_entity_id"
        const val DOB = "dob"
        const val DOD = "dod"
        const val UNIQUE_ID = "unique_id"
        const val VILLAGE_TOWN = "village_town"
        const val DATE_REMOVED = "date_removed"
        const val GENDER = "gender"
        const val DETAILS = "details"
        const val RELATIONAL_ID = "relationalid"
        const val FAMILY_HEAD = "family_head"
        const val PRIMARY_CARE_GIVER = "primary_caregiver"
        const val PRIMARY_CARE_GIVER_PHONE_NUMBER = "pcg_phone_number"
        const val FAMILY_NAME = "family_name"
        const val PHONE_NUMBER = "phone_number"
        const val OTHER_PHONE_NUMBER = "other_phone_number"
        const val FAMILY_HEAD_PHONE_NUMBER = "family_head_phone_number"
        const val TB_REGISTRATION_DATE = "tb_registration_date"
        const val TB_STATUS = "tb_status"
        const val COMMUNITY_CLIENT_TB_REGISTRATION_NUMBER = "community_client_tb_registration_number"
        const val CLIENT_TB_STATUS_DURING_REGISTRATION = "client_tb_status_during_registration"
        const val CLIENT_TB_STATUS_AFTER_TESTING = "client_tb_status_after_testing"
        const val PLACE_OF_DOMICILE = "place_of_domicile"
        const val CLIENT_TB_SCREENING_RESULTS = "client_tb_screening_results"
        const val IS_CLOSED = "is_closed"
        const val FAMILY_MEMBER_ENTITY_TYPE = "entity_type"
        const val CHW_FOLLOWUP_DATE = "chw_followup_date"
        const val TB_COMMUNITY_FOLLOWUP_VISIT_DATE = "tb_community_followup_visit_date"
        const val TB_CASE_CLOSURE_DATE = "tb_case_closure_date"
        const val REASONS_FOR_ISSUING_COMMUNITY_REFERRAL = "reasons_for_issuing_community_referral"
        const val TB_COMMUNITY_REFERRAL_DATE = "tb_community_referral_date"
        const val LAST_INTERACTED_WITH = "last_interacted_with"
        const val LAST_FACILITY_VISIT_DATE = "last_client_visit_date"
        const val COMMENTS = "comment"
        const val COMMUNITY_REFERRAL_FORM_ID = "community_referral_form_id"
        const val CHW_NAME = "chw_name"
    }
}


