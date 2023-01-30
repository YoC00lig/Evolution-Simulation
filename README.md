<h1> Evolution-simulation project 🌎 🌍 🌏 </h1>
<p> Darwinian-evolution-simulation project written as part of the "Object-oriented programming" course. </p>
<p> The application enables the user to create own simulation with selected parameters and monitoring statistics related to the number of deaths, average energy level, free fields etc. </p>
<p> Several simulations can be run simultaneously, so it is possible to keep track and compare results.</p>

<h2>💡 Idea description: </h2>
<p> The idea of this project is based on Darwin's theory. Every day the animals move, eat, 
reproduce and die. Each of them has a certain amount of energy which is lost by performing daily 
activities and gained by eating grass. They have their own genotype assigned at birth that 
determines their next move and which they partially inherit from their parents.</p>

<p> Grass doesn't grow the same everywhere. As in the normal world, it is more likely to grow 
in preferred fields. The location of these fields depends on the preferences selected at the start.</p>

<p> The world is not fair. Is there such a configuration that will help animals survive as 
long as possible? Check it yourself.</p>
<img src="/readme/start.gif">

<h2> 🛠 Core technology stack: </h2>
<ul>
<li> Java 17 </li>
<li> Gradle </li>
<li> JavaFX </li>
</ul>


<h2> 🐌 ☘️ Default simulation demo: </h2>
<p> To get information about the animal you have to click "STOP" button and then click on the animal. </p>
<p> To continue the simulation, click the "START" button. </p>
<p> To close the simulation, click the "EXIT" button. </p>
<img src="/readme/demo.gif">

<h2> 🔎  Input parameters: </h2>
<ul> 
  <li> <strong> Save data </strong> - saving to CSV file</li>
  <li> <strong> Width, height</strong> - map dimensions</li>
  <br>
  <li> <strong>Predistination mode: </strong>
<ul> 
<li> <em>true</em> - Animal's movements are determined by consecutive numbers in the genotype </li>
  <li> <em>false</em> - In 80% of cases, a animal's movements are determined by the next gene in the genotype. In 20% of cases, a random gene is selected. </li>
</ul>
</li>
  <li> <strong> Toxic-dead mode:</strong>
<ul> 
<li> <em>true</em> - Grass grows more often where the fewest animals have died. </li>
<li> <em>false</em> - Grass grows more often at the equator. </li>
</ul>
  <li>  <strong> Is-crazy mode: </strong>
<ul> 
<li> <em>true</em> - Mutation reduces/increases the gene by one. </li>
<li> <em>false</em> - Mutation changes a gene to a randomly selected gene. </li>
</ul>
</li>
</li>
<li> <strong> hell's portal mode: </strong>
<ul> 
  <li> <em>true</em> - When the animal goes beyond the edge of the map, it ends up in a magical portal. There, its energy decreases the same as in the case of reproduction. After that, the animal is moved anywhere on the map.</li>
<li> <em>false</em> - If the animal goes beyond the northern or southern edge, it changes its direction by 180 degrees. If it goes beyond the east/west edge, it will show up on the opposite side.</li>
</ul>
</li>
<br>
<li> <strong>reproduction energy</strong> - The energy an animal needs to be able to reproduce. </li>
<li> <strong>plant energy</strong> - The energy an animal gains from eating a plant. </li>
<li><strong> initial animal energy </strong>- The energy each animal receives at the start of the simulation. </li>
<li> <strong>start number of animals/plants</strong> - Initial number of animals/plants at the start of the simulation. </li>
<li> <strong>number of plants per-day </strong>- Determines how many plants grow each day. </li>
<li> <strong>length of genotype</strong> - Length of each animal's genotype. </li>
<li> <strong>Minimum/maximum number of mutations</strong> - Determines how many genes can be changed as a result of mutation. </li>
<li> <strong>Energy that animal lost in reproduction.</strong> </li>
</ul>
<img src="/readme/input.gif">

<h2> 🔮 Symbols: </h2>
<p> The color of the shadow for each animal determines its energy level. </p>
<h4> Shadow color meaning: </h4>
<ul> 
<li> Green - high energy level </li>
<li> Yellow and orange - medium energy level</li>
<li> Red - low energy level </li>
</ul>
<h4> Pictures meaning: </h4>
<img src="/readme/symbols.png">

<h2> 🎮 How to start? </h2>
<p> 1. Enter your preferences or select "Default simulation" </p>
<p> 2. Click "Confirm" to add simulation. </p>
<p> 3. Place the windows in a convenient for you place. </p>
<p> 4. Click "Play" to start simulations. </p>
<img src="/readme/simulations-demo.gif">

<h2> 🏴‍☠️ End of simulation: </h2>
<p> You can end the simulation at any time by clicking the "EXIT" button.</p>
<p> In case all animals die, the simulation will end automatically. </p>
<img src="/readme/end.gif">

