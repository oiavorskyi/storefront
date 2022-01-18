insert into PRODUCT
    (ID, NAME, DESCRIPTION)
VALUES ('2535d495-33e7-47d1-806f-e0617986d222',
        'Sterling by Music Man StingRay',
        'The Classic, Active, “SLO Special” Styled StingRay Bass.'),
       ('6b8210a4-8f36-484e-800e-77dbab73ccba',
        'Universal Audio OX Reactive Amp Attenuator',
        'A More Flexible Way to Use Your Tube Amplifier'),
       ('f32e5442-2610-4d0c-a266-1dfc646c9d96',
        'Fender American Professional II Telecaster Deluxe',
        'A New Spin on the American Professional Telecaster Deluxe'),
       ('83085b91-5d7d-4301-9119-719f150e879a',
        'Kawai ES920 88-key Digital Piano',
        'Kawai Tone and Touch, Plus Unbeatable Value'),
       ('46b1b19b-9206-4680-a846-727f3db0ac86',
        'EVH 5150III 50-watt Tube Head with EL34 Tubes',
        'EL34-injected Tone in a Lightweight Chassis'),
       ('f5073ebc-aebc-47ac-b4b2-095e3eb5bffe',
        'Peavey Classic 30 II 30-watt Combo Amp',
        'One Seriously Giggable Amplifier!'),
       ('48c1ccf1-ee72-4b90-82f1-e07390578c96',
        'Squier Classic Vibe Stratocaster',
        'The Stratocaster Never Goes Out of Style');

insert into PRODUCT_TAG
    (PRODUCT_ID, TAG)
VALUES
    -- Sterling by Music Man StingRay
    ('2535d495-33e7-47d1-806f-e0617986d222', 'bass guitar'),
    ('2535d495-33e7-47d1-806f-e0617986d222', 'home studio'),
    -- Universal Audio OX Reactive Amp Attenuator
    ('6b8210a4-8f36-484e-800e-77dbab73ccba', 'home studio'),
    ('6b8210a4-8f36-484e-800e-77dbab73ccba', 'fragile'),
    ('6b8210a4-8f36-484e-800e-77dbab73ccba', 'most popular'),
    -- Fender American Professional II Telecaster Deluxe
    ('f32e5442-2610-4d0c-a266-1dfc646c9d96', 'electric guitar'),
    ('f32e5442-2610-4d0c-a266-1dfc646c9d96', 'new release'),
    -- Kawai ES920 88-key Digital Piano
    ('83085b91-5d7d-4301-9119-719f150e879a', 'piano'),
    ('83085b91-5d7d-4301-9119-719f150e879a', 'fragile'),
    ('83085b91-5d7d-4301-9119-719f150e879a', 'bulky'),
    ('83085b91-5d7d-4301-9119-719f150e879a', 'new release'),
    -- EVH 5150III 50-watt Tube Head with EL34 Tubes
    ('46b1b19b-9206-4680-a846-727f3db0ac86', 'amplifier'),
    ('46b1b19b-9206-4680-a846-727f3db0ac86', 'fragile'),
    -- Peavey Classic 30 II 30-watt Combo Amp
    ('f5073ebc-aebc-47ac-b4b2-095e3eb5bffe', 'amplifier'),
    ('f5073ebc-aebc-47ac-b4b2-095e3eb5bffe', 'home studio'),
    ('f5073ebc-aebc-47ac-b4b2-095e3eb5bffe', 'fragile'),
    -- Squier Classic Vibe Stratocaster
    ('48c1ccf1-ee72-4b90-82f1-e07390578c96', 'electric guitar'),
    ('48c1ccf1-ee72-4b90-82f1-e07390578c96', 'most popular'),
    ('48c1ccf1-ee72-4b90-82f1-e07390578c96', 'home studio');



