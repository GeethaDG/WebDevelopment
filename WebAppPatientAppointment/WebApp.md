# Thought Process

Author Note :
- It was exceptionally hard for me to build what I have. But unfortunately, thats exactly not what I was hoping to achieve but due to my technical shortcomings and also time constrains (since, I have a MRI conference paper submission this week, I couldn't allocate as much time as I would like to)
- Irrespective of the result, I am thankful for the oppurtunity, this effort has put me in a place to take it forward as a full stack developer.


Design Though Process and design I wanted to implement : 
- Text Fields at Patient End
- Patient Name - First Name Last Name Text  with permissions for Letters a-z only allowed 
- Purpose of Appointment ->  Text Field
- Suggested Date - > Date Field
- Suggested Time -> Drop Down 

- For suggested Date and Time, using onchange method on Date, I wanted to show only the slots that are available to be booked to the patient 

- UI -> Wanted to add create a view with an option to select if the visiting member is a patient or Doctor or Secretary

- Patient with submit data. 
- Doctor has access to all the patient details patient has entered while booking the appointment 
- Secretary has only access to Last name and Date and time of the appointment
- Above was considered keeping in my mind the data security. 

# Questions

Question : What privacy issues are related to this feature? 
1. Handling Patient details is very important in this case, since its not a secure connection, there is possibility of data theft. 
2. The 'Purpose of Appointment' field is purely for the Doctor and Patient to know and not any third person, hence I tried to address this with the feature of secretary.
3. We could use another db just to store the credentials and secure that anyone cannot access the DB.

Question : Why is health data such as this especially protected by the data protection and privacy regulations  (Datenschutz-Grundverordnung/DSGVO)? 
1. Since the health data contains all the information starting from full name to what is the health issue, the security is extremely important 
2. Transparency is a fundamental principle. Data subjects should be enabled to check the collection, processing or use of data or, as the German Federal Constitutional Court has put it, to know and control "who knows what about them, when and on what occasion". In summary it can be said that the GDPR tries to strengthen the rights of the concerning in principle and even expands these in some areas. In particular, the new transparency and information obligations for companies offer significantly stronger protection for the data subject than the old provisions of the Bundesdatenschutzgesetz (German Federal Data Protection Act, BDSG).
3. Since, I am already working with patient data, I was required to complete the trainings with respect to Data protection, In case its needed, I could share the same.
