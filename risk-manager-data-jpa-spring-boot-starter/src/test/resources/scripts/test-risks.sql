INSERT INTO risk_data (risk_id, severity, probability_of_occurence, description, details, contingency_planning, mitigation_strategy)
VALUES (1, 'HIGH', 'MEDIUM', 'Bad idea', 'If we do that, we get a problem', null ,null);
INSERT INTO risk_data (risk_id, severity, probability_of_occurence, description, details, contingency_planning, mitigation_strategy)
VALUES (2, 'VERY_HIGH', 'HIGH', 'Bad idea', 'If we do that, we get a problem', null ,null);
INSERT INTO risk_data (risk_id, severity, probability_of_occurence, description, details, contingency_planning, mitigation_strategy)
VALUES (3, 'LOW', 'VERY_HIGH', 'Missing Frontend capabilities',
        'The currently only developer is a software-architect and backend specialist, With only rudimentary knowledge of frontend development in general and current web frontend technologies in particular. Because of that, developing a web-framework might become difficult'
           , 'We implement either a simple commandline interface or using other options of presentation',
        'We provide web- and programming interfaces to allow several applications and existing frameworks to connect to this system');
INSERT INTO risk_data (risk_id, severity, probability_of_occurence, description, details, contingency_planning, mitigation_strategy)
VALUES (4, 'HIGH', 'MEDIUM', 'Test risk', 'Some details on the risk',
        'crying and panic', 'praying')