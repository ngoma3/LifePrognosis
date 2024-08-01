import java.time.LocalDate;

public class Patient extends User {
    private LocalDate birthDate;
    private boolean hasChronicDisease;
    private LocalDate chronicDiseaseStartDate;
    private boolean vaccinated;
    private LocalDate vaccinationDate;
    private String country;

    public Patient(String firstName, String lastName, String email, String password, String salt,
                   LocalDate birthDate, boolean hasChronicDisease, LocalDate chronicDiseaseStartDate,
                   boolean vaccinated, LocalDate vaccinationDate, String country) {
        super(firstName, lastName, email, password, UserRole.PATIENT, salt);
        this.birthDate = birthDate;
        this.hasChronicDisease = hasChronicDisease;
        this.chronicDiseaseStartDate = chronicDiseaseStartDate;
        this.vaccinated = vaccinated;
        this.vaccinationDate = vaccinationDate;
        this.country = country;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public boolean isHasChronicDisease() {
        return hasChronicDisease;
    }

    public void setHasChronicDisease(boolean hasChronicDisease) {
        this.hasChronicDisease = hasChronicDisease;
    }

    public LocalDate getChronicDiseaseStartDate() {
        return chronicDiseaseStartDate;
    }

    public void setChronicDiseaseStartDate(LocalDate chronicDiseaseStartDate) {
        this.chronicDiseaseStartDate = chronicDiseaseStartDate;
    }

    public boolean isVaccinated() {
        return vaccinated;
    }

    public void setVaccinated(boolean vaccinated) {
        this.vaccinated = vaccinated;
    }

    public LocalDate getVaccinationDate() {
        return vaccinationDate;
    }

    public void setVaccinationDate(LocalDate vaccinationDate) {
        this.vaccinationDate = vaccinationDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
