package com.example.stoycho.phonebook.communicationClasses.commands;

import android.content.Context;

import com.example.stoycho.phonebook.R;

public class Response {
    // respond statuses
    public static final int RESPOND_STATUS_UNKNOWN							= -1;
    public static final int RESPOND_STATUS_SUCCESS							= 0;
    public static final int RESPOND_STATUS_NO_DATA							= 1;
    public static final int RESPOND_STATUS_SQL								= 3;
    public static final int RESPOND_STATUS_INVALID_IN_PARAM					= 4;
    public static final int RESPOND_STATUS_INVALID_IBAN					    = 5;
    public static final int RESPOND_STATUS_HUP_COMM							= 6;
    public static final int RESPOND_STATUS_ANY								= 7;
    public static final int RESPOND_STATUS_USER_EXIST						= 9;
    public static final int RESPOND_STATUS_USER_NOT_EXIST					= 10;
    public static final int RESPOND_STATUS_INVALID_LOGIN					= 11;
    public static final int RESPOND_STATUS_OPER_NOT_PERMIT					= 12;
    public static final int RESPOND_STATUS_INVALID_SESSION					= 14;
    public static final int RESPOND_STATUS_PIN_TRIES_EXCEED					= 16;
    public static final int RESPOND_STATUS_CASHDESK_ACTIVE					= 23;
    public static final int RESPOND_STATUS_INVALID_CARD						= 26;
    public static final int RESPOND_STATUS_CARD_LIM_EXCEED					= 27;
    public static final int RESPOND_STATUS_CARD_REGISTERED					= 35;
    public static final int RESPOND_STATUS_SAL_STATUS_INVALID				= 43;
    public static final int RESPOND_STATUS_INVALID_DATA						= 44;
    public static final int RESPOND_STATUS_WRONG_VERIFICATION_CODE          = 47;
    public static final int RESPOND_STATUS_INTERNAL							= 48;
    public static final int RESPOND_STATUS_CARD_NOT_CONFIRMED				= 49;
    public static final int RESPOND_STATUS_IPAY_INVALID_MOBNUM_VERIFCODE	= 52;
    public static final int RESPOND_STATUS_SAME_MOBILE_NUMBER               = 58;
    public static final int RESPOND_STATUS_USER_PERM_BLOCKED_DUE_TO_BADPWD	= 60;
    public static final int RESPOND_STATUS_LINK_CARD_STATUS					= 61;
    public static final int RESPOND_STATUS_LINK_CARD_WRONG_VER_STATUS		= 62;
    public static final int RESPOND_STATUS_SAME_USER_DETAILS    			= 63;
    public static final int RESPOND_STATUS_USER_FILE_EXIST      			= 64;
    public static final int RESPOND_STATUS_USER_SAME_MAIL         			= 66;
    public static final int RESPOND_STATUS_CARD_INCORRECT_OR_UNSUPPORTED    = 67;
    public static final int RESPOND_STATUS_TRAN_DECLINED_BY_ISSUER			= 68;
    public static final int RESPOND_STATUS_IPAY_NSF							= 72;
    public static final int RESPOND_STATUS_USER_GSM_EXIST					= 78;
    public static final int RESPOND_STATUS_USER_EMAIL_EXIST                 = 79;
    public static final int	RESPOND_STATUS_LIMIT_CNT_EXCEED                 = 80;
    public static final int RESPOND_STATUS_CARD_ALREADY_REGISTERED          = 82;
    public static final int RESPOND_STATUS_USER_NOT_IDENTIFIED              = 83;
    public static final int RESPOND_STATUS_MAX_CARD_LIMIT_REACHED           = 84;
    public static final int RESPOND_STATUS_BLOCKED_BY_ISSUER                = 85;
    public static final int RESPOND_STATUS_NO_DEFAULT_WALLET                = 86;
    public static final int RESPOND_STATUS_NO_RIGHTS				        = 87;
    public static final int RESPOND_STATUS_NOT_VERIFIED				        = 88;
    public static final int RESPOND_STATUS_WRONG_EMAIL_PASSWORD			    = 97;
    public static final int RESPOND_STATUS_EGN_GSM_NOT_MATCH			    = 100;
    public static final int RESPOND_STATUS_UNSUPPORTED_VERSION			    = 104;
    public static final int RESPOND_STATUS_INVALID_IBAN1			        = 105;
    public static final int RESPOND_STATUS_RECEIVER_NOT_ACTIVE			    = 116;
    public static final int RESPOND_STATUS_RECEIVER_IS_TERMINATED		    = 118;
    public static final int RESPOND_MANDATORY_UPDATE						= 119;
    public static final int RESPOND_ACCOUNT_LIMIT_REACHED				    = 120;
    public static final int RESPOND_STATUS_MAX_CARD_LIMIT_REACHED_VERIFY    = 121;
    //120 za create account max number reached ?

    public static String getErrorMessage(Context context, int status, int command){
        String strResult = context.getString(R.string.operation_failed);

        if( command == AllCommands.COMMAND_LOGIN){
            if( status == RESPOND_STATUS_INVALID_LOGIN)
                strResult = context.getString(R.string.wrong_email_password);
        }
        ///////////////////////////////////////////////////////
        else if( command == AllCommands.COMMAND_CREATE_ACCOUNT){
//            if( status == RESPOND_ACCOUNT_LIMIT_REACHED)
//                strResult = context.getString(R.string.you_have_reached_the_max_number_of_accounts_in_this_currency);
        }
        ///////////////////////////////////////////////////////
        else if( command == AllCommands.COMMAND_ADD_LINK_CARD){
            if(status == RESPOND_STATUS_CARD_ALREADY_REGISTERED){
//                strResult = context.getString(R.string.card_linking);
            }
            else if(status == RESPOND_STATUS_MAX_CARD_LIMIT_REACHED){
//                strResult = context.getString(R.string.user_has_reached_max_card_limit_for_account_type);
            }
            else if(status == RESPOND_STATUS_INVALID_CARD){
//                strResult = context.getString(R.string.incorrect_pan);
            }
            else if(status == RESPOND_STATUS_CARD_REGISTERED){
//                strResult = context.getString(R.string.card_alr_reg);
            }
            else if(status == RESPOND_STATUS_INVALID_IN_PARAM){
//                strResult = context.getString(R.string.the_operation_could_not_be_completed);
            }
            else if(status == RESPOND_STATUS_SQL){
//                strResult = context.getString(R.string.our_appologies);
            }
        }
        ///////////////////////////////////////////////////////
        else if( command == AllCommands.COMMAND_FUND_FROM_LINK_CARD ){
            if(  status == RESPOND_STATUS_NO_DATA )
                strResult = context.getString(R.string.operation_failed);
            else if(  status == RESPOND_STATUS_SQL )
                strResult = context.getString(R.string.operation_failed);
            else if(  status == RESPOND_STATUS_INVALID_IN_PARAM )
                strResult = context.getString(R.string.operation_failed);
            else if(  status == RESPOND_STATUS_ANY )
                strResult = context.getString(R.string.operation_failed);
            else if(  status == RESPOND_STATUS_OPER_NOT_PERMIT )
                strResult = context.getString(R.string.oper_cannot_be_procc_bec_acc_blocked);
            else if(  status == RESPOND_STATUS_INVALID_CARD )
                strResult = context.getString(R.string.operation_cannot_be_proccessed);
            else if(  status == RESPOND_STATUS_CARD_LIM_EXCEED )
                strResult = context.getString(R.string.the_limit_for_amount_of_funding);
            else if(  status == RESPOND_STATUS_INVALID_DATA )
                strResult = context.getString(R.string.operation_not_allowed);
            else if(  status == RESPOND_STATUS_INTERNAL )
                strResult = context.getString(R.string.operation_failed);
            else if(  status == RESPOND_STATUS_TRAN_DECLINED_BY_ISSUER )
                strResult = context.getString(R.string.oper_decl_by_your_issuer);
            else if(  status == RESPOND_STATUS_IPAY_NSF )
                strResult = context.getString(R.string.ins_funds);
            else if(  status == RESPOND_STATUS_LIMIT_CNT_EXCEED )
                strResult = context.getString(R.string.the_limit_for_number_of_funding_has_been_reached);
            else if(  status == RESPOND_STATUS_BLOCKED_BY_ISSUER )
                strResult = context.getString(R.string.for_security_reasons_blocked_wallet);
            else if(  status == RESPOND_STATUS_NO_RIGHTS )
                strResult = context.getString(R.string.you_do_not_have_perm_to_make);
            else if(  status == RESPOND_STATUS_NOT_VERIFIED )
                strResult = context.getString(R.string.invalid_card_status);
        }
        ///////////////////////////////////////////////////////
        else if( command == AllCommands.COMMAND_VERIFY_CARD ){
            if(  status == RESPOND_STATUS_NO_DATA )
                strResult = context.getString(R.string.operation_failed);
            else if(  status == RESPOND_STATUS_SQL )
                strResult = context.getString(R.string.operation_failed);
            else if(  status == RESPOND_STATUS_INVALID_IN_PARAM )
                strResult = context.getString(R.string.operation_failed);
            else if(  status == RESPOND_STATUS_ANY )
                strResult = context.getString(R.string.operation_failed);
            else if(  status == RESPOND_STATUS_INVALID_CARD )
                strResult = context.getString(R.string.card_not_found);
//            else if(  status == RESPOND_STATUS_LINK_CARD_WRONG_VER_STATUS )
//                strResult = context.getString(R.string.wrong_verification_code);
//            else if(  status == RESPOND_STATUS_INVALID_DATA)
//                strResult = context.getString(R.string.operation_cannot_be_processed_because_your_card_is_pend_ver);
//            else
////                strResult = context.getString(R.string.wrong_verification_code);
        }
        ///////////////////////////////////////////////////////
        else if( command == AllCommands.COMMAND_SEND_CONF_CODE ){
            if(  status == RESPOND_STATUS_OPER_NOT_PERMIT )
                strResult = context.getString(R.string.oper_cannot_be_proccessed_contact);
            else if(  status == RESPOND_STATUS_INVALID_CARD )
                strResult = context.getString(R.string.oper_cannot_be_proccessed_contact);
            else if(  status == RESPOND_STATUS_LINK_CARD_STATUS )
                strResult = context.getString(R.string.oper_cannot_be_proccessed_contact);
            else
                strResult = context.getString(R.string.oper_cannot_be_proccessed_contact);
        }
        ///////////////////////////////////////////////////////
        else if( command == AllCommands.COMMAND_LINK_CARD_PARAMS ){
//            if(  status == RESPOND_STATUS_ANY )
//                strResult = context.getString(R.string.incorrect_pan);
//            else if(  status == RESPOND_STATUS_INVALID_SESSION )
////                strResult = context.getString(R.string.session_expired_login_again);
//            else
////                strResult = context.getString(R.string.oper_cannot_be_proccessed_contact);
        }
        ///////////////////////////////////////////////////////
        else if ( command == AllCommands.COMMAND_SAVE_CARD_CONTROL ){
            strResult = context.getString(R.string.oper_cannot_be_proccessed_contact);
        }
        ///////////////////////////////////////////////////////
        else if( command == AllCommands.COMMAND_GET_CARDS_LIMITS ){
            strResult = context.getString(R.string.oper_cannot_be_proccessed_contact);
        }
        ///////////////////////////////////////////////////////
        else if( command == AllCommands.COMMAND_DELETE_ACCOUNT){
            strResult = context.getString(R.string.oper_cannot_be_proccessed_contact);
        }
        ///////////////////////////////////////////////////////
        else if( command == AllCommands.COMMAND_EXCHANGE_MONEY ){
            if(  status == RESPOND_STATUS_SQL )
                strResult = context.getString(R.string.oper_cannot_be_proccessed_contact);
            else if(  status == RESPOND_STATUS_NO_DATA )
                strResult = context.getString(R.string.oper_cannot_be_proccessed_contact);
            else if(  status == RESPOND_STATUS_INVALID_IN_PARAM )
                strResult = context.getString(R.string.oper_cannot_be_proccessed_contact);
            else if(  status == RESPOND_STATUS_ANY )
                strResult = context.getString(R.string.oper_cannot_be_proccessed_contact);
            else if(  status == RESPOND_STATUS_BLOCKED_BY_ISSUER )
                strResult = context.getString(R.string.oper_cannot_be_procc_bec_acc_blocked);
            else if(  status == RESPOND_STATUS_IPAY_NSF )
                strResult = context.getString(R.string.ins_funds);
            else
                strResult = context.getString(R.string.oper_cannot_be_proccessed_contact);
        }
        ///////////////////////////////////////////////////////
        else if( command == AllCommands.COMMAND_SEND_BANK_TRANSFER || command == AllCommands.COMMAND_SEND_MONEY || command == AllCommands.COMMAND_SEND_MONEY_TO_LEUPOST){
            if(  status == RESPOND_STATUS_SQL )
                strResult = context.getString(R.string.oper_cannot_be_proccessed_contact);
            else if(  status == RESPOND_STATUS_NO_DATA )
                strResult = context.getString(R.string.oper_cannot_be_proccessed_contact);
            else if(  status == RESPOND_STATUS_INVALID_IN_PARAM )
                strResult = context.getString(R.string.oper_cannot_be_proccessed_contact);
            else if(  status == RESPOND_STATUS_ANY )
                strResult = context.getString(R.string.oper_cannot_be_proccessed_contact);
            else if(  status == RESPOND_STATUS_BLOCKED_BY_ISSUER )
                strResult = context.getString(R.string.oper_cannot_be_procc_bec_acc_blocked);
            else if(  status == RESPOND_STATUS_OPER_NOT_PERMIT )
                strResult = context.getString(R.string.oper_cannot_be_procc_bec_acc_blocked);
            else if(  status == RESPOND_STATUS_RECEIVER_IS_TERMINATED )
                strResult = context.getString(R.string.receiver_blocked);
            else if(  status == RESPOND_STATUS_IPAY_NSF )
                strResult = context.getString(R.string.ins_funds);
            else if(  status == RESPOND_STATUS_RECEIVER_NOT_ACTIVE )
                strResult = context.getString(R.string.operation_cannot_be_prossed_receiver_not_active);
            else
                strResult = context.getString(R.string.oper_cannot_be_proccessed_contact);
        }
        ///////////////////////////////////////////////////////
        else if( command == AllCommands.COMMAND_ACTIVATE_CARD_OR_TAG ){
            if(  status == RESPOND_STATUS_CARD_INCORRECT_OR_UNSUPPORTED )
                strResult = context.getString(R.string.invalid_card_pan);
            else if(  status == RESPOND_STATUS_INVALID_CARD )
                strResult = context.getString(R.string.the_card_cannot_be_activated_from_your_account);
            else if(  status == RESPOND_STATUS_MAX_CARD_LIMIT_REACHED )
                strResult = context.getString(R.string.you_have_reached_your_card_count_limit);
            else if(  status == RESPOND_STATUS_MAX_CARD_LIMIT_REACHED_VERIFY )
                strResult = context.getString(R.string.you_have_reached_your_card_count_limit_for_unverified_account);
            else if(  status == RESPOND_STATUS_CARD_ALREADY_REGISTERED || status == RESPOND_STATUS_LINK_CARD_STATUS )
                strResult = context.getString(R.string.your_mypos_card_already_been_activated);
            else if ( status == RESPOND_ACCOUNT_LIMIT_REACHED )
                strResult = context.getString(R.string.you_have_reached_your_card_count_limit);
        }
        ///////////////////////////////////////////////////////
        else if( command == AllCommands.COMMAND_CHECK_SMS){
            strResult = context.getString(R.string.wrong_confirmation_code);
        }
        else if( command == AllCommands.COMMAND_SEND_CONFIRMATION_CODE ){
            if ( status == RESPOND_STATUS_NO_DATA )
                strResult = context.getString(R.string.operation_failed);
            else if ( status == RESPOND_STATUS_SQL )
                strResult = context.getString(R.string.operation_failed);
            else if ( status == RESPOND_STATUS_INVALID_IN_PARAM )
                strResult = context.getString(R.string.operation_failed);
            else if ( status == RESPOND_STATUS_OPER_NOT_PERMIT )
                strResult = context.getString(R.string.oper_cannot_be_procc_bec_acc_blocked);
            else if ( status == RESPOND_STATUS_INVALID_CARD )
                strResult = context.getString(R.string.card_not_found);
            else if ( status == RESPOND_STATUS_LINK_CARD_STATUS )
                strResult = context.getString(R.string.operation_cannot_be_processed_because_your_card_is_pend_ver);
            else if ( status == RESPOND_STATUS_TRAN_DECLINED_BY_ISSUER )
                strResult = context.getString(R.string.operation_is_declined_by_some_reason);
        }
        ///////////////////////////////////////////////////////
        else if( command == AllCommands.COMMAND_PROCESS_PREPAID_PAYMENT){
            if(  status == RESPOND_STATUS_SQL )
                strResult = context.getString(R.string.oper_cannot_be_proccessed_contact);
            else if(  status == RESPOND_STATUS_NO_DATA )
                strResult = context.getString(R.string.oper_cannot_be_proccessed_contact);
            else if(  status == RESPOND_STATUS_INVALID_IN_PARAM )
                strResult = context.getString(R.string.oper_cannot_be_proccessed_contact);
            else if(  status == RESPOND_STATUS_ANY )
                strResult = context.getString(R.string.oper_cannot_be_proccessed_contact);
            else if(  status == RESPOND_STATUS_BLOCKED_BY_ISSUER )
                strResult = context.getString(R.string.oper_cannot_be_procc_bec_acc_blocked);
            else if(  status == RESPOND_STATUS_OPER_NOT_PERMIT )
                strResult = context.getString(R.string.oper_cannot_be_procc_bec_acc_blocked);
            else if (status == RESPOND_STATUS_IPAY_NSF)
                strResult = context.getString(R.string.ins_funds);
            else if (status == RESPOND_STATUS_LIMIT_CNT_EXCEED)
                strResult = context.getString(R.string.operation_failed_limits_reached);
            else if (status == RESPOND_STATUS_CARD_LIM_EXCEED)
                strResult = context.getString(R.string.operation_failed_limits_reached);
        }

        return strResult;
    }


//    public static String getLimitExceedErrorMsg(Context context, BaseComponentCommunication command, int status, String fundingCardStatus){
//        try{
//            String periodType;
//            String operation        = "";
//            String maxLimit         = "";
//            LimitModel failedLimit  = new LimitModel();
//
//            if( command instanceof CommandSendMoney) {
//                failedLimit = ((CommandSendMoney) command).getFailedLimit();
//                operation = context.getString(R.string.sending_money);
//            }
//            else if( command instanceof CommandSendMoneyToLeupost ){
//                failedLimit = ((CommandSendMoneyToLeupost) command).getFailedLimit();
//                operation = context.getString(R.string.sending_money);
//            }
//            else if( command instanceof CommandSendBankTransfer ) {
//                failedLimit = ((CommandSendBankTransfer) command).getFailedLimit();
//                operation = context.getString(R.string.sending_money);
//            }
//            else if( command instanceof CommandFundFromLinkCard ) {
//                failedLimit = ((CommandFundFromLinkCard) command).getFailedLimit();
//                operation = context.getString(R.string.funding);
//            }
//
//            periodType = context.getString(R.string.single).toLowerCase();
//            if( failedLimit.getPeriodType().equalsIgnoreCase(Utils.PERIOD_TYPE_DAILY))
//                periodType = context.getString(R.string.daily).toLowerCase();
//            else if( failedLimit.getPeriodType().equalsIgnoreCase(Utils.PERIOD_TYPE_WEEKLY))
//                periodType = context.getString(R.string.weekly).toLowerCase();
//            else if( failedLimit.getPeriodType().equalsIgnoreCase(Utils.PERIOD_TYPE_MONTHLY))
//                periodType = context.getString(R.string.monthly).toLowerCase();
//            else if( failedLimit.getPeriodType().equalsIgnoreCase(Utils.PERIOD_TYPE_ANNUALLY))
//                periodType = context.getString(R.string.annually).toLowerCase();
//
//            if( status == RESPOND_STATUS_CARD_LIM_EXCEED )
//                maxLimit = Utils.formatAmount(failedLimit.getAmountMax()) + " " + GlobalData.getInstance().getmPrimaryCurrency(); //failedLimit.getCurrency();
//            else if(status == RESPOND_STATUS_LIMIT_CNT_EXCEED)
//                maxLimit = failedLimit.getCountMax() + " " + context.getString(R.string.allowed_transactions);
//
//            return context.getString(R.string.you_have_reached_your_period_limit_of_limit_for_operation, periodType, maxLimit, operation);
//        } catch (Exception e){
//            e.printStackTrace();
//            return context.getString(R.string.operation_failed);
//        }
//    }
}
