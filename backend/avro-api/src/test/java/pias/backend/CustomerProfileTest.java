package pias.backend;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;

import java.io.IOException;
import org.apache.avro.ipc.Callback;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.junit.jupiter.MockitoExtension;
import pias.backend.avro.CustomerProfileAvpr;
import pias.backend.avro.CustomerProfileAvro;
import pias.backend.avro.CustomerProfileCreateAvro;
import pias.backend.avro.CustomerProfileUpdateAvro;

@ExtendWith(MockitoExtension.class)
public class CustomerProfileTest {
  public static CustomerProfileAvro RANDOM_CUSTOMER_PROFILE() {
    return CustomerProfileAvro.newBuilder()
        .setId(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .setVersion(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .setEpoch(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .setExternalEmail(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .setExternalLegalName(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .build();
  }

  public static CustomerProfileCreateAvro RANDOM_CUSTOMER_PROFILE_REQUEST() {
    return CustomerProfileCreateAvro.newBuilder()
        .setExternalEmail(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .setExternalLegalName(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .build();
  }

  public static CustomerProfileUpdateAvro RANDOM_CUSTOMER_PROFILE_UPDATE() {
    return CustomerProfileUpdateAvro.newBuilder()
        .setId(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .setLastVersion(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .setExternalEmail(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .setExternalLegalName(AsyncAvroServiceExternalResource.RANDOM_UTF8())
        .build();
  }

  @RegisterExtension
  public AsyncAvroServiceExternalResource<CustomerProfileAvpr.Callback>
      customerProfileServiceAvroServiceExternalResource =
          new AsyncAvroServiceExternalResource<>(CustomerProfileAvpr.Callback.class);

  @Test
  public void testCreate() throws IOException {
    CustomerProfileCreateAvro customerProfileRequest = RANDOM_CUSTOMER_PROFILE_REQUEST();
    CustomerProfileAvro expectedCustomerProfile = RANDOM_CUSTOMER_PROFILE();
    doAnswer(
            invocation -> {
              invocation
                  .<Callback<CustomerProfileAvro>>getArgument(1)
                  .handleResult(expectedCustomerProfile);
              return null;
            })
        .when(customerProfileServiceAvroServiceExternalResource.getService())
        .avroCreateCustomerProfile(eq(customerProfileRequest), any(Callback.class));
    CustomerProfileAvro actualCustomerProfile =
        customerProfileServiceAvroServiceExternalResource
            .getClient()
            .avroCreateCustomerProfile(customerProfileRequest);
    MatcherAssert.assertThat(actualCustomerProfile, CoreMatchers.equalTo(expectedCustomerProfile));
  }

  @Test
  public void testUpdate() throws IOException {
    CustomerProfileUpdateAvro customerProfileUpdate = RANDOM_CUSTOMER_PROFILE_UPDATE();
    CustomerProfileAvro expectedUpdate = RANDOM_CUSTOMER_PROFILE();

    doAnswer(
            invocation -> {
              invocation.<Callback<CustomerProfileAvro>>getArgument(1).handleResult(expectedUpdate);
              return null;
            })
        .when(customerProfileServiceAvroServiceExternalResource.getService())
        .avroUpdateCustomerProfile(eq(customerProfileUpdate), any(Callback.class));
    ;
    CustomerProfileAvro actualCustomerProfile =
        customerProfileServiceAvroServiceExternalResource
            .getClient()
            .avroUpdateCustomerProfile(customerProfileUpdate);
    MatcherAssert.assertThat(actualCustomerProfile, CoreMatchers.equalTo(expectedUpdate));
  }

  @Test
  public void testRead() throws IOException {
    CustomerProfileAvro expectedUpdate = RANDOM_CUSTOMER_PROFILE();
    final String id = expectedUpdate.getId();
    doAnswer(
            invocation -> {
              invocation.<Callback<CustomerProfileAvro>>getArgument(1).handleResult(expectedUpdate);
              return null;
            })
        .when(customerProfileServiceAvroServiceExternalResource.getService())
        .avroReadCustomerProfile(eq(id), any(Callback.class));
    CustomerProfileAvro actualCustomerProfile =
        customerProfileServiceAvroServiceExternalResource.getClient().avroReadCustomerProfile(id);
    MatcherAssert.assertThat(actualCustomerProfile, CoreMatchers.equalTo(expectedUpdate));
  }
}
