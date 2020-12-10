# Thought Process

Author Note :
- It was exceptionally hard for me to build what I have. But unfortunately, thats exactly not what I was hoping to achieve but due to my technical shortcomings and also time constrains (since, I have a MRI conference paper submission this week, I couldn't allocate as much time as I would like to)
- Irrespective of the result, I am thankful for the oppurtunity, this effort has put me in a place to take it forward as a full stack developer.


Design Though Process and design I wanted to implement : 
Text Fields at Patient End
Patient Name - First Name Last Name Text  with permissions for Letters a-z only allowed 
Purpose of Appointment ->  Text Field
Suggested Date - > Date Field
Suggested Time -> Drop Down 

For suggested Date and Time, using onchange method on Date, I wanted to show only the slots that are available to be booked to the patient 

UI -> Wanted to add create a view with an option to select if the visiting member is a patient or Doctor or Secretary

Patient with submit data. 
Doctor has access to all the patient details patient has entered while booking the appointment 
Secretary has only access to Last name and Date and time of the appointment

This I considered keeping in my mind the data security. 
