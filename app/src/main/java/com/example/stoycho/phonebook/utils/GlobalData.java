package com.example.stoycho.phonebook.utils;

public class GlobalData {
    private String mSession;

    //ClIENT DATA
    private String              mIsClientId                     = "";
    private String              mDisplayName                    = "";
    private String              mGsm                            = "";
    private String              mEmail                          = "";
    private String              mBillingCountry                 = "";
    private String              mIdentNbr                       = "";
    private String              mWallletType                    = "";
    private String              mWalletStatus                   = "";
    private String              mCompanyCategoryId              = "";
    private String              mEgfn                           = "";
    private String              mCountry                        = "";
    private String              mCity                           = "";
    private String              mZipCode                        = "";
    private String              mAddress                        = "";
    private String              mSmartAccountId                 = "";
    private String              mWalletNumber                   = "";
    private String              mWalletId                       = "";
    private String              mOwnerName                      = "";
    private String              mOwnerPosition                  = "";
    private String              mOwnerEmail                     = "";
    private String              mOwnerPhone                     = "";
    private long                mSessionStartMillis             = -1;
    private String              mCompanyCategoryDescription     = "";
    private boolean             mHaveLeupost                    = false;
    private boolean             mIsVerified                     = false;
    private boolean             mHaveLinkedCards                = false;
    private int                 mUnreadNotifications            = -1;
    private String              mLoginPhone                     = "";
    private String              mPrimaryCurrency                = "";
    private boolean             mIsMultiUser                    = false;
    private boolean             mCanLinkCard                    = false;
    private float               mTotalBalanceMainCurr           = -1;
    private String              mCountryIsoName                 = "";
    private String              mPhotoGuid                      = "";

    private static GlobalData _instance = null;

    //GLOBAL METHODS
    public static synchronized GlobalData getInstance() {
        if (_instance == null) {
            _instance = new GlobalData();
        }
        return _instance;
    }

    public String getOwnerName() {
        return mOwnerName;
    }

    public void setOwnerName(String mOwnerName) {
        this.mOwnerName = mOwnerName;
    }

    public String getOwnerPosition() {
        return mOwnerPosition;
    }

    public void setOwnerPosition(String mOwnerPosition) {
        this.mOwnerPosition = mOwnerPosition;
    }

    public String getOwnerEmail() {
        return mOwnerEmail;
    }

    public void setOwnerEmail(String mOwnerEmail) {
        this.mOwnerEmail = mOwnerEmail;
    }

    public String getOwnerPhone() {
        return mOwnerPhone;
    }

    public void setOwnerPhone(String mOwnerPhone) {
        this.mOwnerPhone = mOwnerPhone;
    }

    public String getWallletType() {
        return mWallletType;
    }

    public void setWallletType(String mWallletType) {
        this.mWallletType = mWallletType;
    }

    public String getWalletStatus() {
        return mWalletStatus;
    }

    public void setWalletStatus(String mWalletStatus) {
        this.mWalletStatus = mWalletStatus;
    }

    public String getCompanyCategoryId() {
        return mCompanyCategoryId;
    }

    public void setCompanyCategoryId(String mCompanyCategoryId) {
        this.mCompanyCategoryId = mCompanyCategoryId;
    }

    public String getEgfn() {
        return mEgfn;
    }

    public void setEgfn(String mEgfn) {
        this.mEgfn = mEgfn;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String mCity) {
        this.mCity = mCity;
    }

    public String getZipCode() {
        return mZipCode;
    }

    public void setZipCode(String mZipCode) {
        this.mZipCode = mZipCode;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getSmartAccountId() {
        return mSmartAccountId;
    }

    public void setSmartAccountId(String mSmartAccountId) {
        this.mSmartAccountId = mSmartAccountId;
    }

    public String getWalletNumber() {
        return mWalletNumber;
    }

    public void setWalletNumber(String mWalletNumber) {
        this.mWalletNumber = mWalletNumber;
    }

    public String getWalletId() {
        return mWalletId;
    }

    public void setWalletId(String mWalletId) {
        this.mWalletId = mWalletId;
    }

    public String getSession() {
        return mSession;
    }

    public void setSession(String mSession) {
        this.mSession = mSession;
    }

    public String getIsClientId() {
        return mIsClientId;
    }

    public void setIsClientId(String mIsClientId) {
        this.mIsClientId = mIsClientId;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String mDisplayName) {
        this.mDisplayName = mDisplayName;
    }

    public String getGsm() {
        return mGsm;
    }

    public void setGsm(String mGsm) {
        this.mGsm = mGsm;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getBillingCountry() {
        return mBillingCountry;
    }

    public void setBillingCountry(String mBillingCountry) {
        this.mBillingCountry = mBillingCountry;
    }

    public String getIdentNbr() {
        return mIdentNbr;
    }

    public void setIdentNbr(String mIdentNbr) {
        this.mIdentNbr = mIdentNbr;
    }

    public long getmSessionStartMillis() {
        return mSessionStartMillis;
    }

    public void setmSessionStartMillis(long mSessionStartMilliseconds) {
        this.mSessionStartMillis = mSessionStartMilliseconds;
    }

    public void setCompanyCategoryDescription(String mCompanyCategoryDescription) {
        this.mCompanyCategoryDescription = mCompanyCategoryDescription;
    }

    public String getCompanyCategoryDescription() {
        return mCompanyCategoryDescription;
    }

    public boolean ismHaveLeupost() {
        return mHaveLeupost;
    }

    public void setmHaveLeupost(boolean mHaveLeupost) {
        this.mHaveLeupost = mHaveLeupost;
    }

    public boolean isIsVerified() {
        return mIsVerified;
    }

    public void setIsVerified(boolean isVerified) {
        mIsVerified = isVerified;
    }

    public boolean isHaveLinkedCards() {
        return mHaveLinkedCards;
    }

    public void setHaveLinkedCards(boolean haveLinkedCards) {
        mHaveLinkedCards = haveLinkedCards;
    }

    public int getUnreadNotifications() {
        return mUnreadNotifications;
    }

    public void setUnreadNotifications(int unreadNotifications) {
        mUnreadNotifications = unreadNotifications;
    }

    public String getLoginPhone() {
        return mLoginPhone;
    }

    public void setLoginPhone(String loginPhone) {
        mLoginPhone = loginPhone;
    }

    public String getmPrimaryCurrency() {
        return mPrimaryCurrency;
    }

    public void setmPrimaryCurrency(String mPrimaryCurrency) {
        this.mPrimaryCurrency = mPrimaryCurrency;
    }

    public boolean ismIsMultiUser() {
        return mIsMultiUser;
    }

    public void setmIsMultiUser(boolean mIsMultiUser) {
        this.mIsMultiUser = mIsMultiUser;
    }

    public boolean isCanLinkCard() {
        return mCanLinkCard;
    }

    public void setCanLinkCard(boolean mCanLinkCard) {
        this.mCanLinkCard = mCanLinkCard;
    }

    public float getmTotalBalanceMainCurr() {
        return mTotalBalanceMainCurr;
    }

    public void setmTotalBalanceMainCurr(float mTotalBalanceMainCurr) {
        this.mTotalBalanceMainCurr = mTotalBalanceMainCurr;
    }

    public String getCountryIsoName() {
        return mCountryIsoName;
    }

    public void setCountryIsoName(String countryIsoName) {
        this.mCountryIsoName = countryIsoName;
    }

    public String getPhotoGuid () {
        return mPhotoGuid;
    }

    public void setPhotoGuid(String guid) {
        this.mPhotoGuid = guid;
    }
}
