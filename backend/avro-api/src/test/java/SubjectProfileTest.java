import org.apache.avro.AvroRemoteException;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import pias.backend.avro.SubjectProfileAvpr;
import pias.backend.avro.SubjectProfileAvro;
import pias.backend.avro.SubjectProfileCreateAvro;
import pias.backend.avro.SubjectProfileUpdateAvro;

public class SubjectProfileTest {
    public static SubjectProfileAvro RANDOM_PRODUCT_PROFILE() {
        return SubjectProfileAvro.newBuilder()
                .setId(AvroServiceExternalResource.RANDOM_UTF8())
                .setVersion(AvroServiceExternalResource.RANDOM_UTF8())
                .setCustomerProfileId(AvroServiceExternalResource.RANDOM_UTF8())
                .setEpoch(AvroServiceExternalResource.RANDOM_UTF8())
                .setExternalSubjectReference(AvroServiceExternalResource.RANDOM_UTF8())
                .setExternalSubjectName(AvroServiceExternalResource.RANDOM_UTF8())
                .build();
    }

    public static SubjectProfileCreateAvro RANDOM_PRODUCT_PROFILE_REQUEST() {
        return SubjectProfileCreateAvro.newBuilder()
                .setCustomerProfileId(AvroServiceExternalResource.RANDOM_UTF8())
                .setExternalSubjectReference(AvroServiceExternalResource.RANDOM_UTF8())
                .setExternalSubjectName(AvroServiceExternalResource.RANDOM_UTF8())
                .build();
    }

    public static SubjectProfileUpdateAvro RANDOM_PRODUCT_PROFILE_UPDATE() {
        return SubjectProfileUpdateAvro.newBuilder()
                .setId(AvroServiceExternalResource.RANDOM_UTF8())
                .setLastVersion(AvroServiceExternalResource.RANDOM_UTF8())
                .setCustomerProfileId(AvroServiceExternalResource.RANDOM_UTF8())
                .setExternalSubjectReference(AvroServiceExternalResource.RANDOM_UTF8())
                .setExternalSubjectName(AvroServiceExternalResource.RANDOM_UTF8())
                .build();
    }

    @Rule
    public AvroServiceExternalResource<SubjectProfileAvpr> productProfileServiceAvroServiceExternalResource = new AvroServiceExternalResource<>(SubjectProfileAvpr.class);


    @Test
    public void testCreate() throws AvroRemoteException {
        SubjectProfileCreateAvro productProfileRequest = RANDOM_PRODUCT_PROFILE_REQUEST();
        SubjectProfileAvro expectedSubjectProfile = RANDOM_PRODUCT_PROFILE();
        Mockito.doReturn(expectedSubjectProfile).when(productProfileServiceAvroServiceExternalResource.getService()).avroCreateSubjectProfile(productProfileRequest);
        SubjectProfileAvro actualSubjectProfile = productProfileServiceAvroServiceExternalResource.getClient().avroCreateSubjectProfile(productProfileRequest);
        Assert.assertThat(actualSubjectProfile, CoreMatchers.equalTo(expectedSubjectProfile));
    }

    @Test
    public void testUpdate() throws AvroRemoteException {
        SubjectProfileUpdateAvro productProfileUpdate = RANDOM_PRODUCT_PROFILE_UPDATE();
        SubjectProfileAvro expectedUpdate = RANDOM_PRODUCT_PROFILE();
        Mockito.doReturn(expectedUpdate).when(productProfileServiceAvroServiceExternalResource.getService()).avroUpdateSubjectProfile(productProfileUpdate);
        SubjectProfileAvro actualSubjectProfile = productProfileServiceAvroServiceExternalResource.getClient().avroUpdateSubjectProfile(productProfileUpdate);
        Assert.assertThat(actualSubjectProfile, CoreMatchers.equalTo(expectedUpdate));
    }

    @Test
    public void testRead() throws AvroRemoteException {

        final SubjectProfileAvro expectedUpdate = RANDOM_PRODUCT_PROFILE();
        final String id = expectedUpdate.getId();
        Mockito.doReturn(expectedUpdate).when(productProfileServiceAvroServiceExternalResource.getService()).avroReadSubjectProfile(id);
        SubjectProfileAvro actualSubjectProfile = productProfileServiceAvroServiceExternalResource.getClient().avroReadSubjectProfile(id);
        Assert.assertThat(actualSubjectProfile, CoreMatchers.equalTo(expectedUpdate));
    }
}
