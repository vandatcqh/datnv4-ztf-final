package org.example.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.59.0)",
    comments = "Source: otp_service.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class OtpServiceGrpc {

  private OtpServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "otp.service.OtpService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.example.grpc.GenerateOtpRequest,
      org.example.grpc.GenerateOtpResponse> getGenerateOtpMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GenerateOtp",
      requestType = org.example.grpc.GenerateOtpRequest.class,
      responseType = org.example.grpc.GenerateOtpResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.example.grpc.GenerateOtpRequest,
      org.example.grpc.GenerateOtpResponse> getGenerateOtpMethod() {
    io.grpc.MethodDescriptor<org.example.grpc.GenerateOtpRequest, org.example.grpc.GenerateOtpResponse> getGenerateOtpMethod;
    if ((getGenerateOtpMethod = OtpServiceGrpc.getGenerateOtpMethod) == null) {
      synchronized (OtpServiceGrpc.class) {
        if ((getGenerateOtpMethod = OtpServiceGrpc.getGenerateOtpMethod) == null) {
          OtpServiceGrpc.getGenerateOtpMethod = getGenerateOtpMethod =
              io.grpc.MethodDescriptor.<org.example.grpc.GenerateOtpRequest, org.example.grpc.GenerateOtpResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GenerateOtp"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.grpc.GenerateOtpRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.grpc.GenerateOtpResponse.getDefaultInstance()))
              .setSchemaDescriptor(new OtpServiceMethodDescriptorSupplier("GenerateOtp"))
              .build();
        }
      }
    }
    return getGenerateOtpMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.example.grpc.ValidateOtpRequest,
      org.example.grpc.ValidateOtpResponse> getValidateOtpMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ValidateOtp",
      requestType = org.example.grpc.ValidateOtpRequest.class,
      responseType = org.example.grpc.ValidateOtpResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.example.grpc.ValidateOtpRequest,
      org.example.grpc.ValidateOtpResponse> getValidateOtpMethod() {
    io.grpc.MethodDescriptor<org.example.grpc.ValidateOtpRequest, org.example.grpc.ValidateOtpResponse> getValidateOtpMethod;
    if ((getValidateOtpMethod = OtpServiceGrpc.getValidateOtpMethod) == null) {
      synchronized (OtpServiceGrpc.class) {
        if ((getValidateOtpMethod = OtpServiceGrpc.getValidateOtpMethod) == null) {
          OtpServiceGrpc.getValidateOtpMethod = getValidateOtpMethod =
              io.grpc.MethodDescriptor.<org.example.grpc.ValidateOtpRequest, org.example.grpc.ValidateOtpResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ValidateOtp"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.grpc.ValidateOtpRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.grpc.ValidateOtpResponse.getDefaultInstance()))
              .setSchemaDescriptor(new OtpServiceMethodDescriptorSupplier("ValidateOtp"))
              .build();
        }
      }
    }
    return getValidateOtpMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static OtpServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<OtpServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<OtpServiceStub>() {
        @java.lang.Override
        public OtpServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new OtpServiceStub(channel, callOptions);
        }
      };
    return OtpServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static OtpServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<OtpServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<OtpServiceBlockingStub>() {
        @java.lang.Override
        public OtpServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new OtpServiceBlockingStub(channel, callOptions);
        }
      };
    return OtpServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static OtpServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<OtpServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<OtpServiceFutureStub>() {
        @java.lang.Override
        public OtpServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new OtpServiceFutureStub(channel, callOptions);
        }
      };
    return OtpServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void generateOtp(org.example.grpc.GenerateOtpRequest request,
        io.grpc.stub.StreamObserver<org.example.grpc.GenerateOtpResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGenerateOtpMethod(), responseObserver);
    }

    /**
     */
    default void validateOtp(org.example.grpc.ValidateOtpRequest request,
        io.grpc.stub.StreamObserver<org.example.grpc.ValidateOtpResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getValidateOtpMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service OtpService.
   */
  public static abstract class OtpServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return OtpServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service OtpService.
   */
  public static final class OtpServiceStub
      extends io.grpc.stub.AbstractAsyncStub<OtpServiceStub> {
    private OtpServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OtpServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new OtpServiceStub(channel, callOptions);
    }

    /**
     */
    public void generateOtp(org.example.grpc.GenerateOtpRequest request,
        io.grpc.stub.StreamObserver<org.example.grpc.GenerateOtpResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGenerateOtpMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void validateOtp(org.example.grpc.ValidateOtpRequest request,
        io.grpc.stub.StreamObserver<org.example.grpc.ValidateOtpResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getValidateOtpMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service OtpService.
   */
  public static final class OtpServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<OtpServiceBlockingStub> {
    private OtpServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OtpServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new OtpServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public org.example.grpc.GenerateOtpResponse generateOtp(org.example.grpc.GenerateOtpRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGenerateOtpMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.example.grpc.ValidateOtpResponse validateOtp(org.example.grpc.ValidateOtpRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getValidateOtpMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service OtpService.
   */
  public static final class OtpServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<OtpServiceFutureStub> {
    private OtpServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected OtpServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new OtpServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.example.grpc.GenerateOtpResponse> generateOtp(
        org.example.grpc.GenerateOtpRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGenerateOtpMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.example.grpc.ValidateOtpResponse> validateOtp(
        org.example.grpc.ValidateOtpRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getValidateOtpMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GENERATE_OTP = 0;
  private static final int METHODID_VALIDATE_OTP = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GENERATE_OTP:
          serviceImpl.generateOtp((org.example.grpc.GenerateOtpRequest) request,
              (io.grpc.stub.StreamObserver<org.example.grpc.GenerateOtpResponse>) responseObserver);
          break;
        case METHODID_VALIDATE_OTP:
          serviceImpl.validateOtp((org.example.grpc.ValidateOtpRequest) request,
              (io.grpc.stub.StreamObserver<org.example.grpc.ValidateOtpResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getGenerateOtpMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.example.grpc.GenerateOtpRequest,
              org.example.grpc.GenerateOtpResponse>(
                service, METHODID_GENERATE_OTP)))
        .addMethod(
          getValidateOtpMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.example.grpc.ValidateOtpRequest,
              org.example.grpc.ValidateOtpResponse>(
                service, METHODID_VALIDATE_OTP)))
        .build();
  }

  private static abstract class OtpServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    OtpServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.example.grpc.OtpServiceProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("OtpService");
    }
  }

  private static final class OtpServiceFileDescriptorSupplier
      extends OtpServiceBaseDescriptorSupplier {
    OtpServiceFileDescriptorSupplier() {}
  }

  private static final class OtpServiceMethodDescriptorSupplier
      extends OtpServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    OtpServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (OtpServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new OtpServiceFileDescriptorSupplier())
              .addMethod(getGenerateOtpMethod())
              .addMethod(getValidateOtpMethod())
              .build();
        }
      }
    }
    return result;
  }
}
