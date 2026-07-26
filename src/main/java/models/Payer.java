package models;

public class Payer {

    private String englishName;
        private String arabicName;
        private String code;
        private String description;
        private String payerType = "Private";
        private String email;
        private String phone;
        private String country = "Saudi Arabia";
        private String city = "Riyadh";
        // License Number, Preferred Language and Preferred Contact Method are required on
    // the Contact Information step of the Add/Edit Payer wizard. They were added here
    // after live QA on 27/07/2026 showed the wizard cannot proceed past step 2 without
    // them. Preferred Language/Contact Method use sensible defaults matching values
    // verified to work end-to-end; License Number has no sensible default so it is
    // generated per-payer by RandomDataGenerator to guarantee uniqueness.
    private String licenseNumber;
        private String preferredLanguage = "English";
        private String preferredContactMethod = "Both";

    public String getEnglishName() {
                return englishName;
    }

    public void setEnglishName(String englishName) {
                this.englishName = englishName;
    }

    public String getArabicName() {
                return arabicName;
    }

    public void setArabicName(String arabicName) {
                this.arabicName = arabicName;
    }

    public String getCode() {
                return code;
    }

    public void setCode(String code) {
                this.code = code;
    }

    public String getDescription() {
                return description;
    }

    public void setDescription(String description) {
                this.description = description;
    }

    public String getPayerType() {
                return payerType;
    }

    public void setPayerType(String payerType) {
                this.payerType = payerType;
    }

    public String getEmail() {
                return email;
    }

    public void setEmail(String email) {
                this.email = email;
    }

    public String getPhone() {
                return phone;
    }

    public void setPhone(String phone) {
                this.phone = phone;
    }

    public String getCountry() {
                return country;
    }

    public void setCountry(String country) {
                this.country = country;
    }

    public String getCity() {
                return city;
    }

    public void setCity(String city) {
                this.city = city;
    }

    public String getLicenseNumber() {
                return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
                this.licenseNumber = licenseNumber;
    }

    public String getPreferredLanguage() {
                return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
                this.preferredLanguage = preferredLanguage;
    }

    public String getPreferredContactMethod() {
                return preferredContactMethod;
    }

    public void setPreferredContactMethod(String preferredContactMethod) {
                this.preferredContactMethod = preferredContactMethod;
    }

    @Override
        public String toString() {
                    return "Payer{englishName='" + englishName + "', arabicName='" + arabicName
                                        + "', code='" + code + "', payerType='" + payerType + "'}";
        }
}
