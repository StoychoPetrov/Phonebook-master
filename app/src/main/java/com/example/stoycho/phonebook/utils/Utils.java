package com.example.stoycho.phonebook.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stoycho.phonebook.communicationClasses.ConnectXmppServer;

import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.io.File;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public final static String  ANDROID                             = "2";
    public static final String  MYPOS_ORIGINATOR                    = "80";

    public static final int     CARD_NOTIFICATION_ID                = 3345;
    //PRODUCTION
//    public static final String  mServerIpAddress                    = "185.68.44.40";
//    public static final int     mPort                               = 443;
//    public static final String  HOST                                = "xmpp.leupay.eu";
//    public static final int     PORT                                = 5222;
//    public static final String  UPLOAD_FILE_LINK                    = "https://www.leupay.eu/phone/request";
//    public static final String  DOWNLOAD_FILE_LINK                  = "https://www.leupay.eu/en/downloadImages/guid:";
    public static final String  USERNAME_TEST                       = "ivelin.georgiev.test@icard.com";
    public static final String  PASSWORD_TEST                       = "1";

    //DEBUG
    public static final String    HOST                        = "xmpp.ipay.eu";
    public static final int       PORT                        = 5222;
    public static final String    mServerIpAddress            = "82.119.81.86";   // debug Virtual
    public static final int       mPort                       = 10967;            // debug Virtual
    public static final String    UPLOAD_FILE_LINK            = "http://devs.icards.eu/leupay/trunk/phone/request";
    public static final String    DOWNLOAD_FILE_LINK          = "http://devs.icards.eu/leupay/trunk/en/downloadImages/guid:";
//    public static final String    mServerIpAddress            = "82.119.81.84";   // debug Daniel
//    public static final int       mPort                       = 10966;            // debug Daniel

    public static final String      FINANCE_OPERATIONS_COMPONENT        = "leupay." + HOST;
    //public static final String      FINANCE_OPERATIONS_COMPONENT        = "leupay-fin.xmpp.ipay.eu"; // debug Daniel

    //ACCOUNT MENU INDEXES
    public static final int      ACCOUNT_MENU_BANK_WIRE_TRANSFER_DETAILS    = 0;
    public static final int      ACCOUNT_MENU_ACCOUNT_DETAILS               = 1;
    public static final int      ACCOUNT_MENU_ACCOUNT_SETTINGS              = 2;
    public static final int      ACCOUNT_MENU_DELETE_ACCOUNT                = 3;
    public static final int      ACCOUNT_ACTION_BAR_REORDER                 = 4;

    //ACCOUNT MENU INDEXES
    public static final int      CARD_MENU_DETAILS                          = 0;
    public static final int      CARD_MENU_SETTINGS_LIMITS                  = 1;
    public static final int      CARD_MENU_NOTIFICATIONS                    = 2;
    public static final int      CARD_MENU_TRANSACTIONS                     = 3;
    public static final int      CARD_MENU_ACTIVATE                         = 4;

    //TRANSACTION TYPE
    public static final String  TRANSACTION_TYPE_FEE                                = "001";
    public static final String  TRANSACTION_TYPE_CASH_WITHDRAWAL                    = "002";
    public static final String  TRANSACTION_TYPE_OUTGOING_BANK_TRANSFER             = "003";
    public static final String  TRANSACTION_TYPE_BALANCE_TRANSFER                   = "004";
    public static final String  TRANSACTION_TYPE_EMONEY_REDEMPTION                  = "005";
    public static final String  TRANSACTION_TYPE_LOAD_MONEY                         = "006";
    public static final String  TRANSACTION_TYPE_ORIGINAL_CREDIT                    = "007";
    public static final String  TRANSACTION_TYPE_POS_PURCHASE                       = "008";
    public static final String  TRANSACTION_TYPE_ONLINE_PURCHASE                    = "009";
    public static final String  TRANSACTION_TYPE_SEND_MONEY                         = "010";
    public static final String  TRANSACTION_TYPE_REFUND                             = "011";
    public static final String  TRANSACTION_TYPE_MONEY_REQUEST                      = "012";
    public static final String  TRANSACTION_TYPE_SALE                               = "013";
    public static final String  TRANSACTION_TYPE_DIRECT_DEBIT                       = "014";
    public static final String  TRANSACTION_TYPE_PRE_AUTHORIZATION                  = "015";
    public static final String  TRANSACTION_TYPE_MOTO_SALE                          = "016";
    public static final String  TRANSACTION_TYPE_MOTO_REFUND                        = "017";
    public static final String  TRANSACTION_TYPE_MOTO_PRE_AUTHORIZATION             = "018";
    public static final String  TRANSACTION_TYPE_ATM_DEPOSIT                        = "019";
    public static final String  TRANSACTION_TYPE_FUNDING_FROM_LEUPAY                = "020";
    public static final String  TRANSACTION_TYPE_FUNDING_VIA_LEUPAY                 = "021";
    public static final String  TRANSACTION_TYPE_NFC_PAYMENT                        = "022";
    public static final String  TRANSACTION_TYPE_ATM_SURCHARGE                      = "023";
    public static final String  TRANSACTION_TYPE_WITHDRAWAL                         = "024";
    public static final String  TRANSACTION_TYPE_CARD_TOP_UP                        = "025";
    public static final String  TRANSACTION_TYPE_UTILITY_BILLS                      = "026";
    public static final String  TRANSACTION_TYPE_MOBILE_TOP_UP                      = "027";
    public static final String  TRANSACTION_TYPE_BUDGET_FUNDING                     = "028";
    public static final String  TRANSACTION_TYPE_BUDGET_SURPLUS_TRANSFER            = "029";
    public static final String  TRANSACTION_TYPE_TRANSFER_TO_LEUPAY                 = "030";
    public static final String  TRANSACTION_TYPE_BDUGET_FUNDING_FROM_LEUPAY         = "031";
    public static final String  TRANSACTION_TYPE_SURPLUS_TRANSFER_TO_LEUPAY         = "032";
    public static final String  TRANSACTION_TYPE_TRANSFER_TO_LEUPOST                = "033";
    public static final String  TRANSACTION_TYPE_TRANSFER_FROM_LEUPOST              = "034";

    //MPOS TRANSACTIONS TYPES
    public static final String  MPOS_TRANSACTION_TYPE_SALE                          = "00";
    public static final String  MPOS_TRANSACTION_TYPE_VOID                          = "02";
    public static final String  MPOS_TRANSACTION_TYPE_REFUND                        = "20";
    public static final String  MPOS_TRANSACTION_TYPE_PRE_AUTH                      = "50";
    public static final String  MPOS_TRANSACTION_TYPE_MO_TO_PRE_AUTH                = "m50";
    public static final String  MPOS_TRANSACTION_TYPE_PRE_AUTH_COMPLETION           = "500320";
    public static final String  MPOS_TRANSACTION_TYPE_PRE_AUTH_CANCELLATION         = "500400";
    public static final String  MPOS_TRANSACTION_TYPE_MO_TO_SALE                    = "m00";
    public static final String  MPOS_TRANSACTION_TYPE_MO_TO_REFUND                  = "m20";
    public static final String  MPOS_TRANSACTION_TYPE_MO_TO_PRE_AUTH_COMPLETION     = "m500320";
    public static final String  MPOS_TRANSACTION_TYPE_MO_TO_PRE_AUTH_CANCELLATION   = "m500400";
    public static final String  MPOS_TRANSACTION_TYPE_ORIGINAL_CREDIT               = "OC";

    //TRANSACTION INTERNAL TYPES
    public static final int     TRANSACTION_TYPE_PERSONAL                           = 0;
    public static final int     TRANSACTION_TYPE_MPOS                               = 1;
    public static final int     TRANSACTION_TYPE_VIRT_MPOS                          = 2;
    public static final int     TRANSACTION_TYPE_PREAUTHORIZED                      = 3;

    // CARD STATUSES
    public static final String CARD_ACTIVE_STATUS		                            = "0";
    public static final String CARD_LOST_STATUS		                                = "1";
    public static final String CARD_STOLEN_STATUS		                            = "2";
    public static final String CARD_DESTROYED_STATUS                                = "3";
    public static final String CARD_EXPIRED_STATUS		                            = "4";
    public static final String CARD_BLOCKED_ISSUER		                            = "5";
    public static final String CARD_BLOCKED_CARDHOLDER	                            = "6";
    public static final String CARD_BLOCKER_FRAUD		                            = "7";
    public static final String CARD_BLOCKED_PARTNER	                                = "8";
    public static final String CARD_NOT_ACTIVATED		                            = "9";

    // ORDERED CARD STATUSES
    public static final String ORDER_CARD_PENDING_STATUS		                    = "1";
    public static final String ORDER_CARD_APPROVED_STATUS		                    = "2";
    public static final String ORDER_CARD_DECLINED_STATUS                           = "3";
    public static final String ORDER_CARD_ON_HOLD_STATUS		                    = "4";

    // LINKED CARD STATUSES
    public static final String LINKED_CARD_STATUS_NONE                  = "0";
    public static final String LINKED_CARD_STATUS_FORBIDDEN             = "1";
    public static final String LINKED_CARD_STATUS_DISCONTINUED          = "2";
    public static final String LINKED_CARD_STATUS_EXPIRED               = "3";
    public static final String LINKED_CARD_STATUS_PENDING_CONFIRMATION  = "10";
    public static final String LINKED_CARD_STATUS_CONFIRMED             = "11";
    public static final String LINKED_CARD_STATUS_NOT_VERIFIED          = "12";
    public static final String LINKED_CARD_STATUS_AWAITING_APPROVAL     = "13";

    // LINKED CARD EMBOSS NAMES
    public static final String LINKED_UNKNOWN_CARD     = "0";
    public static final String LINKED_ICARD            = "1";
    public static final String LINKED_MASTER_CARD      = "2";
    public static final String LINKED_MAESTRO          = "3";
    public static final String LINKED_VISA             = "4";
    public static final String LINKED_VISA_ELECTRON    = "5";
    public static final String LINKED_VPAY             = "6";
    public static final String LINKED_JCB              = "7";

    // Transaction status
    public static final String PENDING_TRANSACTION		                            = "0";
    public static final String APPROVED_TRANSACTION	                                = "1";
    public static final String DENIED_TRANSACTION		                            = "2";
    public static final String CANCELED_TRANSACTION	                                = "3";

    // Payment status
    public static final String PAYMENT_NOT_SETTLED		                            = "1";
    public static final String PAYMENT_FOR_PAYOUT	                                = "2";
    public static final String PAYMENT_SETTLED		                                = "3";
    public static final String PAYMENT_NOT_TO_BE_SETTLED                            = "4";

    // Transaction receipt send via
    public static final String RECEIPT_SEND_VIA_MOBILE		                    = "0";
    public static final String RECEIPT_SEND_VIA_EMAIL	                        = "1";

    // Virtual button types
    public static final String VIRTUAL_BUTTON		                            = "1";
    public static final String VIRTUAL_LINK	                                    = "2";

    // Card limits operation codes
    public static final String OPERATION_CODE_CASH_WITHDRAW 			        = "201";
    public static final String OPERATION_CODE_POS_PURCHASE 			            = "202";
    public static final String OPERATION_CODE_ONLINE 			                = "203";

    // Card limits periods
    public static final String PERIOD_TYPE_SINGLE                               = "1";
    public static final String PERIOD_TYPE_DAILY                                = "2";
    public static final String PERIOD_TYPE_WEEKLY                               = "3";
    public static final String PERIOD_TYPE_MONTHLY                              = "4";
    public static final String PERIOD_TYPE_ANNUALLY                             = "5";

    // Card limits operation codes
    public static final String TERMINAL_MODEL_D180			                    = "D180";
    public static final String TERMINAL_MODEL_D200			                    = "D200";
    public static final String TERMINAL_MODEL_D210		                        = "D210";
    public static final String TERMINAL_MODEL_D210C	                            = "D210C";


    //CURRENCIES
    public static final String EUR		= "EUR";
    public static final String BGN		= "BGN";
    public static final String GBP		= "GBP";
    public static final String USD		= "USD";
    public static final String HRK		= "HRK";
    public static final String RON		= "RON";
    public static final String CHF		= "CHF";
    public static final String JPY		= "JPY";
    public static final String PLN      = "PLN";

    //SHARED PREFERENCES
    public static final String      PREFERENCES_JID                             = "jid";
    public static final String      PREFERENCES_PASS                            = "password";
    public static final String      PREFERENCES_DISPLAY_NAME                    = "display_name";
    public static final String      PREFERENCES_SHORT_PASS                      = "short_password";
    public static final String      PREFERENCES_SHOW_DEFAULT_WALLET_DIALOG      = "show_default_wallet_dialog";
    public static final String      PREFERENCES_TRANSACTIONS_MAX_AMOUNT         = "transactions_max_amount";
    public static final String      PREFERENCES_POS_TRANSACTIONS_MAX_AMOUNT     = "pos_transactions_max_amount";
    public static final String      PREFERENCES_PREAUTHORIZATIONS_MAX_AMOUNT    = "preauthorizations_max_amount";
    public static final String      PREFERENCES_VIRT_POS_MAX_AMOUNT             = "virt_pos_max_amount";
    public static final String      PREFERENCES_AGREED_WITH_TERMS_OF_USE        = "agreed_with_terms_of_use";
    public static final String      PREFERENCES_BAD_SECRET_TRIES                = "bad_secret_tries";
    public static final String      PREFERENCES_BAD_SECRET_TRIES_TIME           = "bad_secret_tries_time";
    public static final String      PREFERENCES_DEFAULT_LEUPAY_CARD_ID          = "default_leupay_card_id";
    public static final String      PREFERENCES_PUSH_TOKEN_UPDATED              = "push_token_updated";
    public static final String      PREFERENCES_PUSH_TOKEN                      = "push_token";
    public static final String      PREFERENCES_PUSH_NOT_ID                     = "push_notification_id";
    public static final String      PREFERENCES_NOTIFICATION_RINGTONE           = "notification_ringtone";
    public static final String      PREFERENCES_SET_ACCOUNT_COLORS              = "set_account_colors";
    public static final String      PREFERENCES_SHOW_WELCOME_SCREEN             = "show_welcome_screen";

    //Типове документи кодове
    public static final String	DOCUMENT_CODE_NONE                          = "0";
    public static final String	DOCUMENT_CODE_GTC                           = "1";
    public static final String	DOCUMENT_CODE_PRIVACY_POLICY                = "2";
    public static final String	DOCUMENT_CODE_ACCEPTANCE_POLICY             = "3";
    public static final String	DOCUMENT_CODE_RETURN_POLICY                 = "4";
    public static final String	DOCUMENT_CODE_TERMS_OF_USE                  = "5";
    public static final String	DOCUMENT_CODE_ABOUT_MOBILE                  = "6";
    public static final String	DOCUMENT_CODE_ABOUT_WEB                     = "7";
    public static final String	DOCUMENT_CODE_RETURN_MATERIAL_AUTHORIZATION = "8";
    public static final String	DOCUMENT_CODE_TERMS_OF_USE_GK               = "9";
    public static final String	DOCUMENT_CODE_ABOUT_MOBILE_GK               = "10";
    public static final String	DOCUMENT_CODE_TERMINAL_TARIFF               = "11";

    //Типове портфейли
    public static final String	WALLET_TYPE_PERSONAL                        = "001";
    public static final String	WALLET_TYPE_BUSINESS                        = "002";
    public static final String	WALLET_TYPE_SYSTEM                          = "999";

    //Статуси на портфейл
    public static final String	WALLET_STATUS_NONE                          = "-1";
    public static final String	WALLET_STATUS_ACTIVE                        = "0";
    public static final String	WALLET_STATUS_TERMINATED                    = "1";
    public static final String	WALLET_STATUS_BLOCKED_BY_ISSUER_OUTGOING    = "2";
    public static final String	WALLET_STATUS_BLOCKED_BY_OWNER              = "3";
    public static final String	WALLET_STATUS_BLOCKED_ISSUER_ALL            = "4";
    public static final String	WALLET_STATUS_TERMINATED_UPON_ICF           = "5";

    /// Пренос на данни към Activity
    public static final String	TRANSFER_ACCOUNT_DATA                       	= "0";
    public static final String	TRANSFER_TRANASCTION_DATA                   	= "1";
    public static final String	TRANSFER_NEW_LOGIN_DATA                     	= "2";
    public static final String	TRANSFER_ACTIVATE_TERMINAL_FROM_BEGINING    	= "3";
    public static final String	TRANSFER_TRANSACTION_ID                     	= "4";
    public static final String	TRANSFER_TERMINAL_DATA                      	= "5";
    public static final String	TRANSFER_URL                                	= "6";
    public static final String	TRANSFER_ACCOUNTS_IN_CURRENCY_COUNT         	= "7";
    public static final String	TRANSFER_CARD_DATA                          	= "8";
    public static final String	TRANSFER_OUTLET_DATA                        	= "9";
    public static final String	TRANSFER_NEW_TERMINAL_DATA                  	= "10";
    public static final String	TRANSFER_TRANSACTION_INTERNAL_TYPE          	= "11";
    public static final String	TRANSFER_PAYMENT_REFERENCE                  	= "12";
    public static final String	TRANSFER_PAN                                	= "14";
    public static final String	TRANSFER_SET_CURRENT_FRAGMENT               	= "15";
    public static final String	TRANSFER_OPERATION_CODE                     	= "16";
    public static final String	TRANSFER_TERMINAL_DETAILS_OPEN_FROM_ACTIVATION  = "17";
    public static final String	TRANSFER_SYSTEM_NOTIFICATIONS                   = "18";
    public static final String	TRANSFER_NOTIFICATIONS_LIST                     = "20";
    public static final String	TRANSFER_ACCOUNT_NUMBER                         = "21";
    public static final String	TRANSFER_DOCUMENT_TYPE                          = "22";
    public static final String	TRANSFER_NOTIFICATION                           = "23";
    public static final String	TRANSFER_PAYMENT_STATUS                         = "24";
    public static final String	TRANSFER_ACTIVATION_CODE                        = "25";
    public static final String	TRANSFER_CURRENCIES_LIST                        = "26";
    public static final String	TRANSFER_REQUEST_CODE                           = "27";
    public static final String	TRANSFER_FILTER_CURRENCIES                      = "28";
    public static final String	TRANSFER_FILTER_TRANSACTION_TYPES               = "29";
    public static final String	TRANSFER_FILTER_CHOOSE_CURRENCIES_OR_TRAN_TYPES = "30";
    public static final String	TRANSFER_FILTER_OPENED_FROM                     = "31";
    public static final String	TRANSFER_REGISTRATION_DATA                      = "32";
    public static final String	TRANSFER_CARD_ACTIVATION_ACC_NUMBER            	= "33";
    public static final String	TRANSFER_CARD_ACTIVATION_ACC_NAME              	= "34";
    public static final String	TRANSFER_CARD_ACTIVATION_ACC_CURRENCY          	= "35";
    public static final String	TRANSFER_CHECK_FOR_PASSWORD                     = "36";
    public static final String	TRANSFER_AFTER_REGISTRATON                      = "37";
    public static final String	TRANSFER_LINKED_CARD_DATA                       = "38";
    public static final String	TRANSFER_AMOUNT                                 = "39";
    public static final String	TRANSFER_CURRENCY                               = "40";
    public static final String	TRANSFER_BENEFICIARY_NAME                       = "42";
    public static final String	TRANSFER_BENEFICIARY_ADDRESS                    = "43";
    public static final String	TRANSFER_BANK_OF_BENEFICIARY                    = "44";
    public static final String	TRANSFER_BANK_COUNTRY_CODE                      = "45";
    public static final String	TRANSFER_BENEFICIARY_BANK_BIC                   = "46";
    public static final String	TRANSFER_IBAN                                   = "47";
    public static final String	TRANSFER_NEW_ACC_CURRENCY_DATA                  = "48";
    public static final String  TRANSFER_ACCOUNT_NAME                           = "49";
    public static final String  TRANSFER_ACCOUNT_AMOUNT                         = "50";
    public static final String  TRANSFER_ACCOUNT_CURR                           = "51";
    public static final String  TRANSFER_PREPAID_OPERATOR                       = "52";
    public static final String  TRANSFER_PIN_CODE                               = "53";
    public static final String  TRANSFER_PIN_DATE                               = "54";
    public static final String  TRANSFER_PIN_INSTRUCTION                        = "55";
    public static final String  TRANSFER_PIN_RECEIPT                            = "56";

    public static final int     MAX_PIN_TRIES                                   = 5;

    private static Utils            _instance = null;

    private ConnectXmppServer       mConnectXmppServer;
    private XMPPTCPConnection       mXmppConnection;

    //COMPANY POSITIONS
    public static final String      POSITION_NONE          = "0";
    public static final String      POSITION_CEO           = "1";
    public static final String      POSITION_MANAGER       = "2";
    public static final String      POSITION_CFO           = "3";
    public static final String      POSITION_ACCOUNTANT    = "4";
    public static final String      POSITION_SELF_EMPLOYED = "5";
    public static final String      POSITION_OTHER         = "6";


    //NOTIFICATION TYPES
    public static final String      NOTIFICATION_TYPE_TRAN      = "T";
    public static final String      NOTIFICATION_TYPE_UTIL      = "U";
    public static final String      NOTIFICATION_TYPE_SPAM      = "S";
    public static final String      NOTIFICATION_TYPE_SYS       = "Y";
    public static final String      NOTIFICATION_TYPE_RESP      = "R";
    public static final String      NOTIFICATION_TYPE_EXTERN    = "X";

    //NOTIFICATIONS MEDIUMS
    public static final String      NOTIFICATION_MEDIUM_SMS  = "S";
    public static final String      NOTIFICATION_MEDIUM_MAIL = "M";
    public static final String      NOTIFICATION_MEDIUM_PUSH = "P";

    // RECEIPT LOGO TYPES
    public static final String      RECEIPT_LOGO_HEADER_TYPE_NONE       = "1";
    public static final String      RECEIPT_LOGO_HEADER_TYPE_DEFAULT    = "2";
    public static final String      RECEIPT_LOGO_HEADER_TYPE_CUSTOM     = "3";
    public static final String      RECEIPT_LOGO_FOOTER_TYPE_DEFAULT    = "0";
    public static final String      RECEIPT_LOGO_FOOTER_TYPE_CUSTOM     = "1";

    public static final int	        ADULT_AGE				            = 18;

    public static final int	        FIFTHEEN_MINUTES_IN_SECONDS             = 900;
    public static final int	        ONE_MINUTE_IN_SECONDS                   = 60;
    public static final int	        REVEAL_ANIMATION_DURATION_START_DELAY   = 300;
    public static final int	        REVEAL_ANIMATION_DURATION_HIDE_DELAY    = 50;
    public static final int	        SHOW_VIEW_CURRENT_ANIMATION_DURATION    = 300;

    // BANK TRANSFER TYPES
    public static final int         BANK_TRANSFER_SWIFT                     = 0;
    public static final int         BANK_TRANSFER_SEPA                      = 1;
    public static final int         BANK_TRANSFER_BISERA                    = 2;
    public static final int         BANK_TRANSFER_HRK                       = 3;

    // IDENT TYPES
    public static final int         IDENT_TYPE_NONE                         = 0;
    public static final int         IDENT_TYPE_GSM                          = 1;
    public static final int         IDENT_TYPE_EMAIL                        = 2;
    public static final int         IDENT_TYPE_IBAN                         = 3;
    public static final int         IDENT_TYPE_WNUM                         = 4;
    public static final int         IDENT_TYPE_ANUM                         = 5;
    public static final int         IDENT_TYPE_CARD                         = 6;
    public static final int         IDENT_TYPE_SID                          = 7;
    public static final int         IDENT_TYPE_ACCOUNT                      = 8;

    //WALLET PRODUCTS
    public static final String      WALLET_PRODUCT_LEUPAY                   = "001";
    public static final String      WALLET_PRODUCT_MYPOS                    = "002";
    public static final String      WALLET_PRODUCT_LEUPOST                  = "004";

    //NATIONAL CODES
    public static final String      BGR				                    = "bgr";

    //ACTIVITIES RESULTS CODE
    public static final int	        OPEN_LINKED_CARD_DETAILS    = 1;
    public static final int	        LINK_NEW_CARD               = 2;
    public static final int	        EDIT_ACCOUNT_DETAILS        = 3;

    //INTENT TRANSFER DATA
    public static final String      INTENT_TRANSFER_COUNTRY_ISO                 = "country_iso";
    public static final String      INTENT_TRANSFER_COUNTRY_NAME                = "country_name";
    public static final String      INTENT_TRANSFER_COUNTRY_DIAL_CODE           = "country_dial_code";
    public static final String      INTENT_TRANSFER_BUSINESS_ACTIVITY_DATA      = "business_activity_data";
    public static final String      INTENT_TRANSFER_OUTLET_ID                   = "outlet_id";
    public static final String      INTENT_TRANSFER_WALLET_PRODUCTS             = "wallet_products";
    public static final String      INTENT_TRANSFER_NOTIFICATION                = "notification";
    public static final String      INTENT_TRANSFER_LINKED_CARD                 = "linked_card";
    public static final String      INTENT_TRANSFER_STATUS                      = "status";
    public static final String      INTENT_TRANSFER_AVV                         = "avv";
    public static final String      INTENT_TRANSFER_ECI                         = "eci";
    public static final String      INTENT_TRANSFER_XID                         = "xid";
    public static final String      INTENT_TRANSFER_AMOUNT                      = "amount";
    public static final String      INTENT_TRANSFER_CURRENCY                    = "currency";
    public static final String      INTENT_TRANSFER_PHONE_CODE                  = "phone_code";
    public static final String      INTENT_TRANSFER_MERCHANT_COUNTRIES_ONLY     = "merchant_countries_only";
    public static final String      INTENT_TRANSFER_SELECTED_COUNTRY            = "selected_country";
    public static final String      INTENT_TRANSFER_COUNTRIES                   = "countries";

    public static final String      DELETE_REASON_REMAINING_BALANCE     = "1";
    public static final String      DELETE_REASON_HAS_CARDS             = "2";
    public static final String      DELETE_REASON_HAS_CARD_ORDERS       = "3";
    public static final String      DELETE_REASON_LINKED_TO_VMPOS       = "4";
    public static final String      DELETE_REASON_LINKED_TO_MPOS        = "5";
    public static final String      DELETE_REASON_PENDING_MPOS          = "6";
    public static final String      DELETE_REASON_DEFAULT_ACCOUNT       = "7";
    public static final String      DELETE_REASON_LAST_ACCOUNT          = "8";
    public static final String      DELETE_REASON_ONLINE_STORE          = "9";

    public static final String     COMMUNICATION_ERROR_CODE     = "1";
    public static final String     COMMUNICATION_TIMEOUT_CODE   = "2";

    //INTENT EXTRAS
    public static final String      ACCOUNT                         = "account";
    public static final String      ACCOUNTS                        = "accounts";
    public static final String      ACCOUNT_COUNT                   = "account_count";
    public static final String      ACCOUNT_POSITION                = "account_position";
    public static final String      ANIMATION                       = "animation";
    public static final String      TRANSACTIONS                    = "transactions";
    public static final String      COPY_TRANSACTION                = "copy_transaction";
    public static final String      CARD                            = "card";
    public static final String      REFRESH_ACCOUNTS                = "refresh_accounts";
    public static final String      REFRESH_CARDS                   = "refresh_cards";
    public static final String      NAME                            = "name";
    public static final String      NUMBER                          = "number";
    public static final String      CARD_PAN                        = "card_pan";
    public static final String      CARD_COUNT                      = "card_count";
    public static final String      CARD_ID                         = "card_id";
    public static final String      CARD_POSITION                   = "card_position";
    public static final String      CAME_FROM_ACCOUNT_DETAIL        = "came_from_account_details";
    public static final String	    RECIPIENT                       = "recipient";
    public static final String      WALLET_PRODUCT                  = "wallet_product";
    public static final String	    RECIPIENT_IMAGE                 = "recipient_image";
    public static final String      COUNTRY_NAME                    = "country_name";
    public static final String      COUNTRY_ISO3                    = "country_iso3";
    public static final String      ZIP_CODE                        = "zip_code";
    public static final String      CITY                            = "city";
    public static final String      ADDRESS                         = "address";
    public static final String      DELIVERY_VIA                    = "delivery_via";
    public static final String      COME_FROM_LEUPAY_WALLET         = "come_from_leupay_wallet";
    public static final String      PRODUCTION_CODE                 = "production_code";
    public static final String      TARIFF_CODE                     = "tariff_code";
    public static final String      LIMITS_CODE                     = "limits_code";
    public static final String      PAID_CARD                       = "paid_card";
    public static final String      PAID_TOTAL                      = "paid_total";
    public static final String      PROMOTION_ORDER_CARD            = "promotion_order_card";
    public static final String      ORDER_SRC                       = "order_src";
    public static final String      ORDER_FOR_VERIFICATION          = "order_for_verification";


    //DELIVERY CODES
    public static final int         DELIVERY_VIA_COURIER            = 1;
    public static final int         DELIVERY_VIA_POSTAL_SERVICES    = 9;


    //RESULT CODES
    public static final int         INTENT_ACCOUNT_DETAIL           = 1;
    public static final int         INTENT_LEUPAY_CARD_DETAIL       = 2;
    public static final int         INTENT_MY_ACCOUNTS              = 3;
    public static final int         INTENT_ADD_ACCOUNT              = 4;
    public static final int         INTENT_LICENSE_AGREEMENT        = 221;
    public static final int         INTENT_SECURE_3D_VERIFICATION   = 222;
    public static final int         INTENT_SECURE_3D_FUNDING        = 223;
    public static final int         INTENT_CHOOSE_PHOTO             = 224;
    public static final int         INTENT_CROP_PHOTO               = 225;
    public static final int         INTENT_ORDER_PREPAID_CARD       = 226;
    public static final int         INTENT_ORDER_CARD_DETAILS       = 227;
    public static final int         INTENT_CHOOSE_COUNTRY           = 228;
    public static final int         INTENT_WELCOME_SCREEN           = 229;
    public static final int         INTENT_TOPUP_PAYMENT            = 230;
    public static final int         INTENT_HISTORY_FAVOURITE        = 231;

    //URL codes
    public static final String URL_CODE_3D_SECURE_REQUEST                   = "0";
    public static final String URL_CODE_3D_SECURE_RESPONSE                  = "1";

    // PREPAID CARD ORDER SOURCES
    public static final int ORDER_SOURCE_TYPE_VERIFICATION                  = 35;
    public static final int ORDER_SOURCE_LEUPAY_MOBILE                      = 36;

    public static final String LEUPAY_DIR_PATH                              = Environment.getExternalStorageDirectory() + File.separator + "LeuPay" + File.separator;

    public static Dialog    mUpdateDialog;
    public static int       mTextChangedCount;
    public static float     mSumAll                 = 0;
    //GLOBAL METHODS
    public static synchronized Utils getInstance() {
        if (_instance == null) {
            _instance = new Utils();
        }
        return _instance;
    }

    public void setmConnectXmppServer(ConnectXmppServer mConnectXmppServer) {
        this.mConnectXmppServer = mConnectXmppServer;
    }

    public XMPPTCPConnection getmXmppConnection() {
        return mXmppConnection;
    }

    public void setmXmppConnection(XMPPTCPConnection mXmppConnection) {
        this.mXmppConnection = mXmppConnection;
    }

    public static void showErrorToast(final Context context, final String message){
        try {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
                    TextView textView = (TextView) toast.getView().findViewById(android.R.id.message);
                    if (textView != null) textView.setGravity(Gravity.CENTER);
                    toast.show();
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void hideAnimationStart(final View animView, long duration){
        animView.animate()
                .alpha(0f)
                .setDuration(duration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animView.setVisibility(View.GONE);
                    }
                });
    }

    public static boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet6Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String md5(final String s)
    {
        String hex = "";

        if (s.isEmpty()) {
            return hex;
        }

        try
        {
            MessageDigest digest = java.security.MessageDigest .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }

            hex = hexString.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hex;
    }

    public static boolean isNetworkConnectionAvailable(Context context) {
        boolean isConnected = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                isConnected = true;
            } else {
                isConnected = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isConnected;
    }

    public static void setButtonSize(Button button, Context context ){
        double  nButtonWidth    = context.getResources().getDisplayMetrics().widthPixels/1.3;
        int     nButtonHeight   = context.getResources().getDisplayMetrics().heightPixels/15;

        button.setWidth((int)nButtonWidth);
        button.setHeight(nButtonHeight);
    }

    public static String formatAmount(String amount){
        try {
            String pattern = "0.00";
            NumberFormat numberFormat = new DecimalFormat(pattern);

            if (!amount.equalsIgnoreCase("")) {
                double dAmntFrom = Double.parseDouble(amount);
                return numberFormat.format(dAmntFrom).replace(',', '.');
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

//    public static String getCountryZipCode(Context context){
//        String countryID;
//        String countryZipCode   = "";
//
//        countryID   = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSimCountryIso().toUpperCase();
//        String[] countryCodes = context.getResources().getStringArray(R.array.CountryCodes);
//
//        for(int i=0;i<countryCodes.length;i++){
//            String[] g=countryCodes[i].split(",");
//            if(g[1].trim().equals(countryID.trim())){
//                countryZipCode=g[0];
//                break;
//            }
//        }
//        return countryZipCode;
//    }
//
//    public static String normalizePhoneNumber(Context context, String phoneNumber){
//        String phoneNumberNormalized;
//
//        if (phoneNumber.trim().startsWith("00"))
//            phoneNumberNormalized = phoneNumber.replaceFirst("00", "+");
//        else if (phoneNumber.trim().startsWith("0")) {
//            if (!Utils.getCountryZipCode(context).equalsIgnoreCase(""))
//                phoneNumberNormalized = "+" + Utils.getCountryZipCode(context) + phoneNumber.trim().substring(1);
//            else
//                phoneNumberNormalized = phoneNumber.trim();
//        } else
//            phoneNumberNormalized = phoneNumber.trim();
//
//        return phoneNumberNormalized;
//    }

    public static void setAmountEditTextPadding(Context context, EditText editText) {
        int paddingLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics());
        int paddingRight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics());
        int paddingTop = editText.getPaddingTop();
        int paddingBottom = editText.getPaddingBottom();
        editText.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    public static float calculateTextSize(int density, float textSize){
        int avrgDensity = 480;
        if(density < avrgDensity) {
            float newTextSize = 70 * textSize / 100;
            newTextSize += (float) density / avrgDensity * 70 * textSize / 100;
            return newTextSize;
        }
        return textSize;
    }

//    public static void addEdiTextInputDigits(final EditText editText, final Context context, final String pattern) {
//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Pattern ps = Pattern.compile(pattern);
//                Matcher ms = ps.matcher(s);
//                boolean bs = ms.matches();
//                if (!bs) {
//                    int subtsTo = editText.getText().toString().length();
//
//                    if (subtsTo > 0) {
//                        subtsTo = subtsTo - 1;
//
//                        editText.setText(editText.getText().toString().substring(0, subtsTo));
//                        int position = editText.length();
//                        Editable etext = editText.getText();
//                        Selection.setSelection(etext, position);
//                        mTextChangedCount++;
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (mTextChangedCount > 0) {
//                                    showErrorToast(context, context.getString(R.string.not_allowed_symbol));
//                                    mTextChangedCount = 0;
//                                }
//                            }
//                        }, 50);
//                    }
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//    }

    public static ArrayList<Integer> calculatePercents(ArrayList<Float> values){
        ArrayList<Integer>  roundedPercents  = new ArrayList<>();
        ArrayList<Float>    restValues       = new ArrayList<>();
        for ( int i = 0 ; i < values.size(); i++ ) {
            float percent   = values.get(i) * 100 / mSumAll;
            roundedPercents.add((int)Math.floor(percent));
            restValues.add(percent % 1);
        }

        int percentsSum = 0;
        for ( int j = 0 ; j < roundedPercents.size(); j++) {
            percentsSum += roundedPercents.get(j);
        }

        int diff = 100 - percentsSum;
        while(diff != 0){
            int index = findBiggestRestValue(restValues);
            roundedPercents.set(index, roundedPercents.get(index) + 1);
            restValues.set(index, 0f);
            diff--;
        }

/*        for ( int i = 1 ; i < values.size(); i++ ) {
            float percent   = values.get(i) * 100 / mSumAll;
            roundedPercents.add(i, (int)Math.floor(percent));
        }*/

        return roundedPercents;
    }

    private static int findBiggestRestValue(ArrayList<Float> restValues){
        int index = 0;
        float max = 0;
        for( int i = 0; i < restValues.size(); i++ ){
            if( max < restValues.get(i)) {
                max = restValues.get(i);
                index = i;
            }
        }

        return index;
    }

    public static void setSumAll(float sumAll){
        mSumAll = sumAll;
    }

    public static boolean checkIfToday(String dateToCheck){
        Date curDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        String dateToStr;
        try {
            dateToStr = dateFormat.format(curDate);
            return dateToCheck.equalsIgnoreCase(dateToStr);
        }catch (Exception e){
            return false;
        }
    }
}
