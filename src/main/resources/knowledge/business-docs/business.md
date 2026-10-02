# NexusFlow — Business Overview

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