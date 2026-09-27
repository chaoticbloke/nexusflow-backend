# NexusFlow

NexusFlow is a full-stack business operations platform built to manage
customers, invoices, and business workflows.

## Technology Stack

- Frontend: Angular
- Backend: Spring Boot
- Database: PostgreSQL
- Cache: Redis
- Messaging: Apache Kafka
- Deployment: Docker and AWS

## Customer Module

The Customer module manages customer information within NexusFlow.

It provides APIs for creating, retrieving, updating, and managing customers.

## Invoice Module

The Invoice module manages invoices associated with customers.

It is responsible for creating and tracking invoices and their business state.

## Authentication

NexusFlow uses JWT-based authentication.

Access tokens are short-lived and are used to access protected business APIs.

Refresh tokens are persisted and can be revoked.

## Event-Driven Architecture

NexusFlow uses Apache Kafka for asynchronous communication between services.

For example, when a user registers, NexusFlow publishes a UserRegisteredEvent.

The email service consumes this event and can process the corresponding email operation.

## AI Assistant

NexusFlow contains an AI assistant.

The AI assistant uses an LLM to understand user questions and generate responses.

Conversation context allows the assistant to use previous messages within a conversation.

The planned architecture also includes RAG so that the AI assistant can retrieve
NexusFlow-specific knowledge when answering questions.