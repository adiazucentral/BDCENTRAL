package net.cltech.enterprisent.domain.masters.test;

/**
 * Represnta un perfil con un examen hijo
 *
 * @version 1.0.0
 * @author jrodriguez
 * @since 28/09/2018
 * @see Creacion
 */
public class Profile
{

    private int profileId;
    private String profileName;
    private int profileState;
    private int testId;
    private String testName;
    private int testState;
    private int testType;
    private int idSample;
    
    public int getProfileId()
    {
        return profileId;
    }

    public void setProfileId(int profileId)
    {
        this.profileId = profileId;
    }

    public String getProfileName()
    {
        return profileName;
    }

    public void setProfileName(String profileName)
    {
        this.profileName = profileName;
    }

    public int getProfileState()
    {
        return profileState;
    }

    public void setProfileState(int profileState)
    {
        this.profileState = profileState;
    }

    public int getTestId()
    {
        return testId;
    }

    public void setTestId(int testId)
    {
        this.testId = testId;
    }

    public String getTestName()
    {
        return testName;
    }

    public void setTestName(String testName)
    {
        this.testName = testName;
    }

    public int getTestState()
    {
        return testState;
    }

    public void setTestState(int testState)
    {
        this.testState = testState;
    }

    public int getTestType()
    {
        return testType;
    }

    public void setTestType(int testType)
    {
        this.testType = testType;
    }
    public int getIdSample()
    {
        return idSample;
    }

    public void setIdSample(int idSample)
    {
        this.idSample = idSample;
    }
}
