package pias.backend;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;

import java.io.IOException;
import org.apache.avro.ipc.Callback;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import pias.backend.avro.SubjectProfileAvpr;
import pias.backend.avro.SubjectProfileAvro;
import pias.backend.avro.SubjectProfileCreateAvro;
import pias.backend.avro.SubjectProfileUpdateAvro;

public class SubjectProfileTest {
  public static SubjectProfileAvro RANDOM_PRODUCT_PROFILE() {
    return SubjectProfileAvro.newBuilder()
        .setId(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .setVersion(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .setCustomerProfileId(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .setEpoch(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .setExternalSubjectReference(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .setExternalSubjectName(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .build();
  }

  public static SubjectProfileCreateAvro RANDOM_PRODUCT_PROFILE_REQUEST() {
    return SubjectProfileCreateAvro.newBuilder()
        .setCustomerProfileId(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .setExternalSubjectReference(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .setExternalSubjectName(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .build();
  }

  public static SubjectProfileUpdateAvro RANDOM_PRODUCT_PROFILE_UPDATE() {
    return SubjectProfileUpdateAvro.newBuilder()
        .setId(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .setLastVersion(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .setCustomerProfileId(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .setExternalSubjectReference(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .setExternalSubjectName(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .build();
  }

  @RegisterExtension
  public AsyncAvroServiceExternalResource<SubjectProfileAvpr.Callback>
      productProfileServiceAvroServiceExternalResource =
          new AsyncAvroServiceExternalResource<>(SubjectProfileAvpr.Callback.class);

  @Test
  public void testCreate() throws IOException {
    SubjectProfileCreateAvro productProfileRequest = RANDOM_PRODUCT_PROFILE_REQUEST();
    SubjectProfileAvro expectedSubjectProfile = RANDOM_PRODUCT_PROFILE();
    doAnswer(
            invocationOnMock -> {
              invocationOnMock
                  .<Callback<SubjectProfileAvro>>getArgument(1)
                  .handleResult(expectedSubjectProfile);
              return null;
            })
        .when(productProfileServiceAvroServiceExternalResource.getService())
        .avroCreateSubjectProfile(eq(productProfileRequest), any(Callback.class));
    SubjectProfileAvro actualSubjectProfile =
        productProfileServiceAvroServiceExternalResource
            .getClient()
            .avroCreateSubjectProfile(productProfileRequest);
    assertThat(actualSubjectProfile, equalTo(expectedSubjectProfile));
  }

  @Test
  public void testUpdate() throws IOException {
    SubjectProfileUpdateAvro productProfileUpdate = RANDOM_PRODUCT_PROFILE_UPDATE();
    SubjectProfileAvro expectedUpdate = RANDOM_PRODUCT_PROFILE();
    doAnswer(
            invocationOnMock -> {
              invocationOnMock
                  .<Callback<SubjectProfileAvro>>getArgument(1)
                  .handleResult(expectedUpdate);
              return null;
            })
        .when(productProfileServiceAvroServiceExternalResource.getService())
        .avroUpdateSubjectProfile(eq(productProfileUpdate), any(Callback.class));
    SubjectProfileAvro actualSubjectProfile =
        productProfileServiceAvroServiceExternalResource
            .getClient()
            .avroUpdateSubjectProfile(productProfileUpdate);
    assertThat(actualSubjectProfile, equalTo(expectedUpdate));
  }

  @Test
  public void testRead() throws IOException {

    final SubjectProfileAvro subjectProfileAvro = RANDOM_PRODUCT_PROFILE();
    final String id = subjectProfileAvro.getId();
    doAnswer(
            invocationOnMock -> {
              invocationOnMock
                  .<Callback<SubjectProfileAvro>>getArgument(1)
                  .handleResult(subjectProfileAvro);
              return null;
            })
        .when(productProfileServiceAvroServiceExternalResource.getService())
        .avroReadSubjectProfile(eq(id), any(Callback.class));
    SubjectProfileAvro actualSubjectProfile =
        productProfileServiceAvroServiceExternalResource.getClient().avroReadSubjectProfile(id);
    assertThat(actualSubjectProfile, equalTo(subjectProfileAvro));
  }
}
