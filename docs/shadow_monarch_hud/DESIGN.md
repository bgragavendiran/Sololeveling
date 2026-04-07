# Design System Specification: The Kinetic HUD

## 1. Overview & Creative North Star
**Creative North Star: "The Sovereign Interface"**
This design system is not a mere utility; it is a digital manifestation of power and progression. Inspired by the high-stakes aesthetic of modern manhwa and hunter-system interfaces, the UI must feel like a sentient, aggressive HUD (Heads-Up Display) that exists between the user and reality.

To break the "template" look, we move away from soft, rounded web standards toward **Aggressive Angularity**. We utilize intentional asymmetry—where one corner of a container might be clipped while others remain sharp—and high-contrast typography scales that prioritize "System Messages" over standard "Content." The layout should feel like a series of glass-like overlays floating in a deep, pressurized void.

---

## 2. Colors: The Void and the Glow
The palette is built on high-contrast "Active Elements" against an infinite dark background.

*   **Primary (`#a9c7ff` / `#2E8CFF`):** The "System Blue." This is the lifeblood of the interface. Use it for Rank progression, active quest states, and "Living" HUD elements.
*   **Secondary/Error (`#FF2E2E`):** The "Penalty Red." Reserved for high-stakes warnings, failed requirements, and system-level threats.
*   **Surface (`#050507`):** "Deep Void Black." The canvas upon which the system lives.

**The "No-Line" Rule:**
Standard 1px borders are strictly prohibited for sectioning. Use **Surface Hierarchy** to define space. A `surface-container-high` card sits on a `surface` background without a stroke. The boundary is defined by the shift in darkness, not a line.

**The "Glass & Gradient" Rule:**
To achieve the "System Window" feel, all floating modals and overlays must use Glassmorphism.
*   **Token:** `surface-container` at 60% opacity with a `20px` backdrop-blur.
*   **Signature Texture:** Apply a subtle linear gradient to main CTAs—transitioning from `primary` (#2E8CFF) to `primary-container` (#3b90ff) at a 45-degree angle—to simulate a glowing energy source.

---

## 3. Typography: Technical Authority
We use a dual-typeface system to separate narrative "System" voice from technical "Data."

*   **Headers (Rajdhani):** Bold, condensed, and aggressive. Used for `display-lg` through `headline-sm`. These should be set in **All Caps** for Rank titles and Quest headers to convey urgency.
*   **Technical Data (Share Tech Mono):** Used for `label-md`, `label-sm`, and historical data points. This font represents the "code" of the system.
*   **Body (Inter):** Reserved for long-form narrative or descriptive text where readability is paramount.

**Hierarchy Strategy:**
The typography should feel like an editorial layout. Use `display-lg` for current Rank (e.g., "RANK: S") and pair it with a `label-sm` technical timestamp to create a high-contrast, premium information density.

---

## 4. Elevation & Depth: Tonal Layering
Traditional drop shadows are too "soft" for this system. We use **Tonal Layering** and **Ambient Glows.**

*   **The Layering Principle:** Stack `surface-container-lowest` for background environments, `surface-container-low` for secondary content, and `surface-container-highest` for active interactive elements.
*   **Ambient Shadows:** For floating HUD elements, use a "Glow Shadow" instead of black. Use the `primary` blue token at 10% opacity with a `40px` blur. It should look like the window is emitting light onto the void behind it.
*   **The "Ghost Border" Fallback:** If a separation is required, use a clipped-corner path with a `0.5px` stroke of `outline-variant` at 20% opacity. It should look like a laser-etched hair-line.

---

## 5. Components: HUD Elements

### Buttons & CTAs
*   **Visual Style:** Sharp 0px corners. Use a "clipped corner" clip-path (bottom-right) for Primary buttons.
*   **Primary:** Background: `primary` (#2E8CFF). Text: `on-primary` (Black).
*   **Secondary:** Background: transparent. Border: 1px `primary`. Text: `primary`.
*   **States:** On hover, the button should "pulse" with an outer glow (`surface_tint`).

### Onboarding Forms (The "System Initialization")
*   **Input Fields:** No boxes. Use a single bottom-weighted line (2px `primary-container`). Labels must be in `Share Tech Mono`.
*   **Validation:** Error states should trigger a flicker animation into `secondary` (Penalty Red).

### Historical Data Charts (The "Evolution Log")
*   **Style:** Remove all grid lines.
*   **Trend Lines:** Use a stepped-line chart (no curves) to represent Rank progression. The area under the line should be a vertical gradient from `primary` (20% opacity) to transparent.
*   **Markers:** Use diamond-shaped markers for "Level Up" events.

### Rank Progression Tracker
*   **Concept:** Replace all currency/gold references with a vertical Rank Bar.
*   **Visual:** A segmented bar where each segment is a sharp-angled parallelogram. Active segments glow; inactive segments are `surface-container-highest`.

---

## 6. Do’s and Don’ts

**Do:**
*   **Do** use asymmetrical layouts. Place critical system alerts slightly off-center to create a sense of dynamic energy.
*   **Do** use "Share Tech Mono" for any numerical value (HP, MP, Rank Percentile).
*   **Do** lean into the "Void." Large areas of `#050507` are necessary to make the blue glows feel premium.

**Don't:**
*   **Don't** use border-radius. Every corner must be 0px (sharp) or clipped at a 45-degree angle.
*   **Don't** mention "Gold," "Coins," or "Shop." The system rewards only Power and Rank.
*   **Don't** use standard icons. Use geometric, HUD-style SVG icons with thin strokes and sharp terminals.
*   **Don't** use dividers. If two pieces of content need separation, use a 32px vertical gap or a slight shift from `surface-container-low` to `surface-container-high`.