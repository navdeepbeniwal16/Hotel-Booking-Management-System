# SWEN90007-2022-LANS
## Project details
**SWEN90007** Software Design and Architecture

Semester 2, 2022

**Submission**: Part 3, Concurrency

**Links**:
- Deployed app: [https://swen90007-2022-lans.herokuapp.com/](https://swen90007-2022-lans.herokuapp.com/)
- Repository: [https://github.com/SWEN900072022/SWEN90007-2022-LANS/tags](https://github.com/SWEN900072022/SWEN90007-2022-LANS/tags)

## Team details
| Name                     | Email                               |
|--------------------------|-------------------------------------|
| Mohammad Saood Abbasi    | mohammadsaoo@student.unimelb.edu.au |
| Arman Arethna            | aarethna@student.unimelb.edu.au     |
| Navdeep Beniwal          | nbeniwal@student.unimelb.edu.au     |
| Levi McKenzie-Kirkbright | levim@student.unimelb.edu.au        |

# Documentation
Submission checklist: /docs/part3/checklist.pdf
Software Architecture and Design Report: /docs/part3/Part_3_LANS.pdf
Database explanation and Data Entry Document: /docs/part3/data-sample.pdf
Testing Document: /docs/part3/testing-document.pdf
Non-concurrent tests: /docs/part3/NonConcurrentTestCases.xlsx
Class diagrams: see /docs/class-diagrams
# Instructions
The navbar is the primary mode of navigation. The navbar will render different options for each user depending on their assigned role (Admin, Hotelier, and Customer).

## Admin
Navigate to the Admin Dashboard via the navbar at the top of the page.
Use the admin dashboard to:
1.	Elevate customers to hoteliers
2.	Add and remove hoteliers to/from groups.
      a.	Relevant data will not be displayed in a hotelier’s dashboard until they are added to a group.
3.	Delist hotels
4.	Create hotel groups

## Hotelier
Navigate to the Hotelier Dashboard via the navbar at the top of the page.
Use the hotelier dashboard to:
1.	View hotels in a hotel group 
2.  Create new hotels for a hotel group
3.  Add rooms to a hotel
4.  View and cancel bookings for each hotel in the hotel group

## Customer
Search for stays on the home page or manage your bookings via My Bookings, which can be found in the navbar at the top of the page.
As a customer you can:
1.	Search for stays
2.	View available rooms in hotels
3.	Create bookings at hotels
4.	View and modify existing bookings
      a.	Change dates
      b.	Update the number of guests
      c.	Cancel

## Log in and account creation
1. Click the "Log in / Sign up" button in the navbar, top right corner of the application.
2. You will be redirected to a Auth0 log in page.
3. If you wish to LOG IN with an existing account, simply enter your email and password.
4. If you wish to SIGN UP, click the "Sign up" link below the username and password fields. Follow the sign up prompts, including entering an email and password for the new account.
5. Once signup or log in is authorised by auth0 you will be redirected to the homepage of our application.

## Existing testing accounts for teaching staff
We have created several accounts for teaching staff to interact with our system and test our system’s concurrency handling. If teaching staff do not want to use these accounts, i.e., they wish to test our system by create new accounts, please proceed to create accounts using standard account creation in the Auth0 redirect window. All customer accounts can be upgraded to hotelier accounts via the admin dashboard.
Please be aware that users cannot be elevated to admin through the frontend system.

| Role             | Username             | Password     |
|------------------|----------------------|--------------|
| Admin #1         | admin@admin.com      | Admin123$    |
| Admin #2         | admin2@admin.com     | Admin123$    |
| Luke Hotelier    | luke@hotelier.com    | Hotelier123$ |
| Max Hotelier     | max@hotelier.com     | Hotelier123$ |
| Eduardo Hotelier | eduardo@hotelier.com | Hotelier123$ |
| Luke Customer    | luke@customer.com    | Customer123$ |
| Max Customer     | max@customer.com     | Customer123$ |
| Eduardo Customer | eduardo@customer.com | Customer123$ |