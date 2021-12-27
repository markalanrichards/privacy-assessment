import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import pias.backend.avro.CustomerProfileAvpr;
import pias.backend.avro.CustomerProfileAvro;
import pias.backend.avro.CustomerProfileCreateAvro;
import pias.backend.avro.CustomerProfileUpdateAvro;

@RunWith(MockitoJUnitRunner.class)
public class CustomerProfileTest {
  public static CustomerProfileAvro RANDOM_CUSTOMER_PROFILE() {
    return CustomerProfileAvro.newBuilder()
        .setId(AvroServiceExternalResource.RANDOM_UTF8())
        .setVersion(AvroServiceExternalResource.RANDOM_UTF8())
        .setEpoch(AvroServiceExternalResource.RANDOM_UTF8())
        .setExternalEmail(AvroServiceExternalResource.RANDOM_UTF8())
        .setExternalLegalName(AvroServiceExternalResource.RANDOM_UTF8())
        .build();
  }

  public static CustomerProfileCreateAvro RANDOM_CUSTOMER_PROFILE_REQUEST() {
    return CustomerProfileCreateAvro.newBuilder()
        .setExternalEmail(AvroServiceExternalResource.RANDOM_UTF8())
        .setExternalLegalName(AvroServiceExternalResource.RANDOM_UTF8())
        .build();
  }

  public static CustomerProfileUpdateAvro RANDOM_CUSTOMER_PROFILE_UPDATE() {
    return CustomerProfileUpdateAvro.newBuilder()
        .setId(AvroServiceExternalResource.RANDOM_UTF8())
        .setLastVersion(AvroServiceExternalResource.RANDOM_UTF8())
        .setExternalEmail(AvroServiceExternalResource.RANDOM_UTF8())
        .setExternalLegalName(AvroServiceExternalResource.RANDOM_UTF8())
        .build();
  }

  @Rule
  public HttpAvroServiceExternalResource<CustomerProfileAvpr>
      customerProfileServiceAvroServiceExternalResource =
          new HttpAvroServiceExternalResource<>(CustomerProfileAvpr.class);

  @Test
  public void testCreate() {
    CustomerProfileCreateAvro customerProfileRequest = RANDOM_CUSTOMER_PROFILE_REQUEST();
    CustomerProfileAvro expectedCustomerProfile = RANDOM_CUSTOMER_PROFILE();
    Mockito.doReturn(expectedCustomerProfile)
        .when(customerProfileServiceAvroServiceExternalResource.getService())
        .avroCreateCustomerProfile(customerProfileRequest);
    CustomerProfileAvro actualCustomerProfile =
        customerProfileServiceAvroServiceExternalResource
            .getClient()
            .avroCreateCustomerProfile(customerProfileRequest);
    MatcherAssert.assertThat(actualCustomerProfile, CoreMatchers.equalTo(expectedCustomerProfile));
  }

  @Test
  public void testUpdate() {
    CustomerProfileUpdateAvro customerProfileUpdate = RANDOM_CUSTOMER_PROFILE_UPDATE();
    CustomerProfileAvro expectedUpdate = RANDOM_CUSTOMER_PROFILE();
    Mockito.doReturn(expectedUpdate)
        .when(customerProfileServiceAvroServiceExternalResource.getService())
        .avroUpdateCustomerProfile(customerProfileUpdate);
    CustomerProfileAvro actualCustomerProfile =
        customerProfileServiceAvroServiceExternalResource
            .getClient()
            .avroUpdateCustomerProfile(customerProfileUpdate);
    MatcherAssert.assertThat(actualCustomerProfile, CoreMatchers.equalTo(expectedUpdate));
  }

  @Test
  public void testRead() {

    CustomerProfileAvro expectedUpdate = RANDOM_CUSTOMER_PROFILE();
    final String id = expectedUpdate.getId();
    CustomerProfileAvpr mockService =
        customerProfileServiceAvroServiceExternalResource.getService();
    Mockito.doReturn(expectedUpdate).when(mockService).avroReadCustomerProfile(id);
    CustomerProfileAvro actualCustomerProfile =
        customerProfileServiceAvroServiceExternalResource.getClient().avroReadCustomerProfile(id);
    MatcherAssert.assertThat(actualCustomerProfile, CoreMatchers.equalTo(expectedUpdate));
  }
}
