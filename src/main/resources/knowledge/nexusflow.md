# NexusFlow

NexusFlow is a business operations platform for organizations
that need to manage their customers, invoices, and business workflows
from a centralized system.

## Core Concept

An organization represents a company that uses NexusFlow.

An organization can have multiple employees (users), with different
roles and responsibilities.

The organization also manages its own customers (clients) within
NexusFlow.

Customers can have invoices associated with them.

### Example

Organization: Acme Technologies

Users:
- Aditya — Admin
- Rahul — Sales
- Priya — Finance

Customers:
- Stripe
- Razorpay
- PhonePe

Invoices:
- Invoice #1001 → Stripe
- Invoice #1002 → Razorpay
- Invoice #1003 → PhonePe

### Mental model

                         ORGANIZATION
                    (Company using NexusFlow)
                              │
              ┌───────────────┼────────────────┐
              │               │                │
              ▼               ▼                ▼
            USERS         CUSTOMERS        WORKFLOWS
          (Employees)      (Clients)
                              │
                              ▼
                           INVOICES

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